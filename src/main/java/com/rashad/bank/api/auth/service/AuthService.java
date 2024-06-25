package com.rashad.bank.api.auth.service;

import com.rashad.bank.api.auth.dto.request.LoginRequest;
import com.rashad.bank.api.auth.dto.request.RegisterRequest;
import com.rashad.bank.api.auth.dto.response.LoginResponse;
import com.rashad.bank.dto.CustomResponse;

import java.io.IOException;
import java.util.Locale;

public interface AuthService {

    CustomResponse register(RegisterRequest request, Locale locale);
    LoginResponse loginUser(LoginRequest request);
}
