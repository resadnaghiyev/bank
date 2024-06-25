package com.rashad.bank.api.auth.service;

import com.rashad.bank.api.auth.dto.request.LoginRequest;
import com.rashad.bank.api.auth.dto.request.RegisterRequest;
import com.rashad.bank.api.auth.dto.response.LoginResponse;
import com.rashad.bank.api.auth.entity.User;
import com.rashad.bank.api.auth.repository.UserRepository;
import com.rashad.bank.dto.CustomResponse;
import com.rashad.bank.exception.*;
import com.rashad.bank.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    @Override
    public CustomResponse register(RegisterRequest request, Locale locale) {
        if (!Validator.checkPassword(request.getPassword())) {
            throw new WrongPasswordFormatException(ErrorCode.WRONG_PASSWORD_FORMAT, "wrong-password-format");
        }
        if (userRepository.existsByPin(request.getPin())) {
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "user-exist-email");
        }

        User user = User.builder()
                .pin(request.getPin())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return CustomResponse.builder().data(user.getId()).build();
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByPin(request.getPin()).orElseThrow(
                () -> new UserNotFoundException(request.getPin()));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPin(), request.getPassword()));
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .expirationDate(jwtService.extractExpiration(accessToken).getTime())
                .userId(user.getId())
                .pin(user.getPin())
                .build();
    }
}