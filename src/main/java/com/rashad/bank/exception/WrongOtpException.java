package com.rashad.bank.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WrongOtpException extends RuntimeException {

    private final String code;

    private final String message;

    public WrongOtpException(String code, String message) {
        super(code);
        this.code = code;
        this.message = message;
    }
}
