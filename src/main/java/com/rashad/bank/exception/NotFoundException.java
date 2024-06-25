package com.rashad.bank.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends RuntimeException {

    private final String code;

    private final String message;

    private final Object[] args;

    public NotFoundException(String code, String message, Object[] args) {
        super(code);
        this.code = code;
        this.message = message;
        this.args = args;
    }

}
