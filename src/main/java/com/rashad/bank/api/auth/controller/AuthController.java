package com.rashad.bank.api.auth.controller;

import com.rashad.bank.api.auth.dto.request.LoginRequest;
import com.rashad.bank.api.auth.dto.request.RegisterRequest;
import com.rashad.bank.api.auth.dto.response.LoginResponse;
import com.rashad.bank.api.auth.service.AuthService;
import com.rashad.bank.dto.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "1.1. Login and Register")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, Locale locale) {
        return new ResponseEntity<>(authService.register(request, locale), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse tokens = authService.loginUser(request);
        CustomResponse customResponse = CustomResponse.builder().data(tokens).build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @GetMapping("/private/test")
    public ResponseEntity<?> privateTest() {
        CustomResponse customResponse = CustomResponse.builder().data("Private Test API").build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }

    @GetMapping("/public/test")
    public ResponseEntity<?> publicTest() {
        CustomResponse customResponse = CustomResponse.builder().data("Public Test API").build();
        return new ResponseEntity<>(customResponse, HttpStatus.OK);
    }
}
