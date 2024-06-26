package com.rashad.bank.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(String arg) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "account-not-found", new Object[]{arg});
    }
}
