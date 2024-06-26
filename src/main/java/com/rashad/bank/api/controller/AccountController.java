package com.rashad.bank.api.controller;

import com.rashad.bank.api.dto.request.AddAccountRequest;
import com.rashad.bank.api.dto.request.TransferRequest;
import com.rashad.bank.api.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "2. Account")
@SecurityRequirement(name = "BearerJwt")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> addAccount(@RequestBody @Valid AddAccountRequest request,
                                        Authentication auth, Locale locale) {
        return new ResponseEntity<>(accountService.addAccount(auth, request, locale), HttpStatus.CREATED);
    }

    @PostMapping("/{number}/deActivate")
    public ResponseEntity<?> deActivateAccount(@PathVariable String number, Authentication auth, Locale locale) {
        return new ResponseEntity<>(accountService.deActivateAccount(auth, number, locale), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAccounts(Authentication auth, Locale locale) {
        return new ResponseEntity<>(accountService.getAccounts(auth, locale), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferToAccount(@RequestBody @Valid TransferRequest request,
                                               Authentication auth, Locale locale) {
        return new ResponseEntity<>(accountService.transferToAccount(auth, request, locale), HttpStatus.OK);
    }
}
