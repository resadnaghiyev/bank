package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.LoginRequest;
import com.rashad.bank.api.dto.request.RegisterRequest;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.api.dto.response.LoginResponse;
import com.rashad.bank.api.entity.User;
import com.rashad.bank.api.repository.UserRepository;
import com.rashad.bank.exception.AlreadyExistsException;
import com.rashad.bank.exception.WrongPasswordFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_Register_Success() {
        RegisterRequest request = RegisterRequest.builder().pin("1234").password("ValidPassword123!").build();
        User user = User.builder().pin("1234").password("encodedPassword").build();
        User registeredUser = User.builder().id(1L).pin("1234").password("encodedPassword").build();

        when(userRepository.existsByPin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(registeredUser);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("User is created successfully");

        CustomResponse response = authService.register(request, Locale.ENGLISH);

        assertNotNull(response);
        assertEquals("User is created successfully", response.getMessage());
        assertEquals(user.getPin(), registeredUser.getPin());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void test_Register_UserAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder().pin("1234").password("ValidPassword123!").build();

        when(userRepository.existsByPin(anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> authService.register(request, Locale.ENGLISH));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_Register_WrongPasswordFormat() {
        RegisterRequest request = RegisterRequest.builder().pin("1234").password("invalid").build();

        assertThrows(WrongPasswordFormatException.class, () -> authService.register(request, Locale.ENGLISH));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_LoginUser_Success() {
        LoginRequest request = LoginRequest.builder().pin("1234").password("ValidPassword123!").build();
        User user = User.builder().id(1L).pin("1234").password("encodedPassword").build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateAccessToken(any())).thenReturn("mockedAccessToken");
        when(jwtService.extractExpiration("mockedAccessToken")).thenReturn(new Date());

        CustomResponse response = authService.loginUser(request, Locale.ENGLISH);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertInstanceOf(LoginResponse.class, response.getData());

        LoginResponse loginResponse = (LoginResponse) response.getData();
        assertEquals("mockedAccessToken", loginResponse.getAccessToken());
        assertEquals(1L, loginResponse.getUserId());
        assertEquals("1234", loginResponse.getPin());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateAccessToken(any());
        verify(jwtService, times(1)).extractExpiration(anyString());
    }

    @Test
    void test_LoginUser_AuthenticationFailure() {
        LoginRequest request = LoginRequest.builder().pin("1234").password("ValidPassword123!").build();

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        AuthenticationException thrown = assertThrows(AuthenticationException.class, () -> authService.loginUser(request, Locale.ENGLISH));

        assertEquals("Bad credentials", thrown.getMessage());

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoInteractions(jwtService);
    }
}