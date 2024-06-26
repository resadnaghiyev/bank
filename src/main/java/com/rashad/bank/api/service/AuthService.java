package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.LoginRequest;
import com.rashad.bank.api.dto.request.RegisterRequest;
import com.rashad.bank.api.dto.response.LoginResponse;
import com.rashad.bank.api.dto.response.CustomResponse;

import java.util.Locale;

public interface AuthService {

    CustomResponse register(RegisterRequest request, Locale locale);
    CustomResponse loginUser(LoginRequest request, Locale locale);
}
