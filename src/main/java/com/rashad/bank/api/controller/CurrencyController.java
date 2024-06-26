package com.rashad.bank.api.controller;

import com.rashad.bank.api.dto.request.CurrencyRequest;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.api.service.CurrencyRateService;
import com.rashad.bank.exception.ErrorCode;
import com.rashad.bank.exception.NotFoundException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/currency")
@Tag(name = "3. Currency Rate")
@SecurityRequirement(name = "BearerJwt")
public class CurrencyController {

    private final CurrencyRateService currencyRateService;

    public CurrencyController(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @PostMapping
    public ResponseEntity<?> getRateByPair(@RequestBody CurrencyRequest request, Locale locale) {
        Double pair = currencyRateService.getRateByPair(request.getPair());
        if (pair == 0) {
            throw new NotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "currency-not-found", new Object[]{request.getPair()});
        }
        CustomResponse body = CustomResponse.builder().data(Map.of(request.getPair(), pair)).build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllRates(Locale locale) {
        Map<String, Double> pair = currencyRateService.getAllRates();
        return new ResponseEntity<>(CustomResponse.builder().data(pair).build(), HttpStatus.OK);
    }
}
