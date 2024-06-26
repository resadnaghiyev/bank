package com.rashad.bank.api.controller;

import com.rashad.bank.api.dto.request.LoginRequest;
import com.rashad.bank.api.dto.request.RegisterRequest;
import com.rashad.bank.api.service.AuthService;
import com.rashad.bank.api.dto.response.CustomResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "1. Login and Register")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, Locale locale) {
        return new ResponseEntity<>(authService.register(request, locale), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request, Locale locale) {
        return new ResponseEntity<>(authService.loginUser(request, locale), HttpStatus.OK);
    }

    @GetMapping("/private/test")
    @SecurityRequirement(name = "BearerJwt")
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
