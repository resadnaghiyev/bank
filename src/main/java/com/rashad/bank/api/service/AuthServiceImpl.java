package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.LoginRequest;
import com.rashad.bank.api.dto.request.RegisterRequest;
import com.rashad.bank.api.dto.response.LoginResponse;
import com.rashad.bank.api.entity.User;
import com.rashad.bank.api.repository.UserRepository;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.exception.*;
import com.rashad.bank.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
            throw new AlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS, "user-exist-pin");
        }

        User user = User.builder()
                .pin(request.getPin())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        String message = messageSource.getMessage("user-created", null, locale);
        return CustomResponse.builder().message(message).build();
    }

    @Override
    public CustomResponse loginUser(LoginRequest request, Locale locale) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPin(), request.getPassword())
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails.getUser());

        LoginResponse tokens = LoginResponse.builder()
                .accessToken(accessToken)
                .expirationDate(jwtService.extractExpiration(accessToken).getTime())
                .userId(userDetails.getId())
                .pin(userDetails.getUsername())
                .build();
        return CustomResponse.builder().data(tokens).build();
    }

}