package com.rashad.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
