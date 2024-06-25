package com.rashad.bank.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String arg) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "user-not-found", new Object[]{arg});
    }
}
