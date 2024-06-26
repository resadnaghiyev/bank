package com.rashad.bank.api.mapper;

import com.rashad.bank.api.dto.response.AccountResponse;
import com.rashad.bank.api.entity.Account;
import com.rashad.bank.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private AccountMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AccountMapper();
    }

    @Test
    public void should_throw_not_found_exception_if_account_is_null() {
        assertThrows(AccountNotFoundException.class, () -> mapper.toAccountResponse(null));
    }

    @Test
    public void should_map_account_to_account_response_dto() {
        Account account = Account.builder()
                .number("12345")
                .balance(BigDecimal.valueOf(12.67))
                .active(true)
                .build();

        AccountResponse response = mapper.toAccountResponse(account);

        assertEquals(response.getNumber(), account.getNumber());
        assertEquals(response.getBalance(), account.getBalance());
        assertEquals(response.getActive(), account.getActive());
    }
}