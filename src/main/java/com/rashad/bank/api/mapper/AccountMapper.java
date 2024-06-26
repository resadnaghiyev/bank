package com.rashad.bank.api.mapper;

import com.rashad.bank.api.dto.response.AccountResponse;
import com.rashad.bank.api.entity.Account;
import com.rashad.bank.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountMapper {

    public AccountResponse toAccountResponse(Account account) {
        if (account == null) {
            throw new AccountNotFoundException("0");
        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setNumber(account.getNumber());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setActive(account.getActive());
        return accountResponse;
    }
}
