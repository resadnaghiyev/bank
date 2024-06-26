package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.AddAccountRequest;
import com.rashad.bank.api.dto.request.TransferRequest;
import com.rashad.bank.api.dto.response.AccountResponse;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.api.entity.Account;
import com.rashad.bank.api.entity.User;
import com.rashad.bank.api.mapper.AccountMapper;
import com.rashad.bank.api.repository.AccountRepository;
import com.rashad.bank.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final MessageSource messageSource;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Override
    public CustomResponse getAccounts(Authentication auth, Locale locale) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Account> accounts = accountRepository.findByUserIdAndActiveIsTrue(userDetails.getId());
        List<AccountResponse> accountResponses = accounts.stream().map(accountMapper::toAccountResponse).toList();
        return CustomResponse.builder().data(accountResponses).build();
    }

    @Override
    public CustomResponse addAccount(Authentication auth, AddAccountRequest request, Locale locale) {

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        if (accountRepository.existsByNumber(request.getAccountNumber())) {
            throw new AlreadyExistsException(ErrorCode.ACCOUNT_ALREADY_EXISTS, "account-exist");
        }
        Account account = Account.builder()
                .number(request.getAccountNumber())
                .balance(request.getBalance())
                .active(true)
                .user(user)
                .build();
        accountRepository.save(account);
        String message = messageSource.getMessage("account-created", null, locale);
        return CustomResponse.builder().message(message).build();
    }

    @Override
    public CustomResponse deActivateAccount(Authentication auth, String number, Locale locale) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Account account = accountRepository.findByNumberAndUserIdAndActiveIsTrue(number, userDetails.getId()).orElseThrow(() ->
                new AccountNotFoundException(number));
        account.setActive(false);
        accountRepository.save(account);
        String message = messageSource.getMessage("account-de-activated", null, locale);
        return CustomResponse.builder().message(message).build();
    }

    @Override
    @Transactional
    public CustomResponse transferToAccount(Authentication auth, TransferRequest request, Locale locale) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        if (Objects.equals(request.getFromAccountNumber(), request.getToAccountNumber())) {
            throw new BadRequestException(ErrorCode.SAME_ACCOUNT, "same-account", null);
        }
        Account fromAccount = accountRepository.findByNumberAndUserIdAndActiveIsTrue(
                request.getFromAccountNumber(), userDetails.getId()).orElseThrow(() ->
                new AccountNotFoundException(request.getFromAccountNumber()));

        Account toAccount = accountRepository.findByNumber(request.getToAccountNumber()).orElseThrow(() ->
                new AccountNotFoundException(request.getToAccountNumber()));

        if (!toAccount.getActive()) {
            throw new BadRequestException(ErrorCode.DEACTIVE_ACCOUNT, "deactive-account", new Object[]{request.getToAccountNumber()});
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BadRequestException(ErrorCode.INSUFFICIENT_BALANCE, "insufficient-balance", null);
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String message = messageSource.getMessage("transfer-success", null, locale);
        return CustomResponse.builder().message(message).build();
    }
}
