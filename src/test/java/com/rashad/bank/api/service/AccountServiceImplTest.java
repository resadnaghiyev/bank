package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.TransferRequest;
import com.rashad.bank.api.dto.response.AccountResponse;
import com.rashad.bank.api.dto.response.CustomResponse;
import com.rashad.bank.api.entity.Account;
import com.rashad.bank.api.entity.User;
import com.rashad.bank.api.mapper.AccountMapper;
import com.rashad.bank.api.repository.AccountRepository;
import com.rashad.bank.exception.AccountNotFoundException;
import com.rashad.bank.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private Authentication authentication;

    @Mock
    private MessageSource messageSource;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    private User user;
    private UserDetailsImpl userDetails;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).pin("1234").password("encodedPassword").build();
        userDetails = new UserDetailsImpl(user);

        fromAccount = new Account(1L, "1234", BigDecimal.valueOf(1000), true, user);
        toAccount = new Account(2L, "5678", BigDecimal.valueOf(500), true, user);
    }

    @Test
    void test_GetAccounts_Success() {
        List<Account> mockAccounts = Arrays.asList(
                new Account(1L, "1234", BigDecimal.valueOf(650.45), true, user),
                new Account(2L, "5678", BigDecimal.valueOf(12.67), true, user)
        );

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByUserIdAndActiveIsTrue(userDetails.getId())).thenReturn(mockAccounts);
        when(accountMapper.toAccountResponse(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            return new AccountResponse(account.getId(), account.getNumber(), account.getBalance(), account.getActive());
        });

        CustomResponse response = accountService.getAccounts(authentication, Locale.ENGLISH);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertInstanceOf(List.class, response.getData());

        @SuppressWarnings("unchecked")
        List<AccountResponse> accountResponses = (List<AccountResponse>) response.getData();
        assertEquals(2, accountResponses.size());
        assertEquals(accountResponses.get(0).getNumber(), mockAccounts.get(0).getNumber());
        assertEquals(accountResponses.get(1).getNumber(), mockAccounts.get(1).getNumber());

        verify(authentication, times(1)).getPrincipal();
        verify(accountRepository, times(1)).findByUserIdAndActiveIsTrue(userDetails.getId());
        verify(accountMapper, times(2)).toAccountResponse(any(Account.class));
    }

    @Test
    void test_GetAccounts_FilterByUser_And_ActiveIsTrue() {
        User otherUser = User.builder().id(2L).pin("5678").password("otherEncodedPassword").build();

        List<Account> mockAccounts = Arrays.asList(
                new Account(1L, "1234", BigDecimal.valueOf(650.45), true, user),
                new Account(2L, "5678", BigDecimal.valueOf(12.67), true, otherUser),
                new Account(3L, "9012", BigDecimal.valueOf(1000.00), true, user),
                new Account(4L, "1314", BigDecimal.valueOf(1000.00), false, user)
        );

        List<Account> currentUserAccounts = mockAccounts.stream()
                .filter(account -> account.getUser().equals(user) && account.getActive())
                .collect(Collectors.toList());

        List<AccountResponse> expectedResponses = currentUserAccounts.stream()
                .map(account -> new AccountResponse(account.getId(), account.getNumber(), account.getBalance(), account.getActive()))
                .toList();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByUserIdAndActiveIsTrue(user.getId())).thenReturn(currentUserAccounts);
        when(accountMapper.toAccountResponse(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            return new AccountResponse(account.getId(), account.getNumber(), account.getBalance(), account.getActive());
        });

        CustomResponse response = accountService.getAccounts(authentication, Locale.ENGLISH);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertInstanceOf(List.class, response.getData());

        @SuppressWarnings("unchecked")
        List<AccountResponse> accountResponses = (List<AccountResponse>) response.getData();
        assertEquals(expectedResponses.size(), accountResponses.size());

        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i).getNumber(), accountResponses.get(i).getNumber());
        }

        verify(authentication, times(1)).getPrincipal();
        verify(accountRepository, times(1)).findByUserIdAndActiveIsTrue(user.getId());
        verify(accountMapper, times(expectedResponses.size())).toAccountResponse(any(Account.class));
    }

    @Test
    @Transactional
    void test_TransferToAccount_Success() {
        TransferRequest request = new TransferRequest("1234", "5678", BigDecimal.valueOf(200));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByNumberAndUserIdAndActiveIsTrue("1234", user.getId())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByNumber("5678")).thenReturn(Optional.of(toAccount));
        when(messageSource.getMessage("transfer-success", null, Locale.ENGLISH)).thenReturn("Transfer successful");

        CustomResponse response = accountService.transferToAccount(authentication, request, Locale.ENGLISH);

        assertNotNull(response);
        assertEquals("Transfer successful", response.getMessage());
        assertEquals(BigDecimal.valueOf(800), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(700), toAccount.getBalance());

        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }

    @Test
    void test_TransferToAccount_SameAccount() {
        TransferRequest request = new TransferRequest("1234", "1234", BigDecimal.valueOf(200));

        when(authentication.getPrincipal()).thenReturn(userDetails);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                accountService.transferToAccount(authentication, request, Locale.ENGLISH));

        assertEquals("same-account", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void test_TransferToAccount_FromAccountNotFound() {
        TransferRequest request = new TransferRequest("1234", "5678", BigDecimal.valueOf(200));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByNumberAndUserIdAndActiveIsTrue("1234", user.getId())).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.transferToAccount(authentication, request, Locale.ENGLISH));

        assertEquals("account-not-found", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void test_TransferToAccount_ToAccountNotFound() {
        TransferRequest request = new TransferRequest("1234", "5678", BigDecimal.valueOf(200));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByNumberAndUserIdAndActiveIsTrue("1234", user.getId())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByNumber("5678")).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.transferToAccount(authentication, request, Locale.ENGLISH));

        assertEquals("account-not-found", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void test_TransferToAccount_ToAccountInactive() {
        toAccount.setActive(false);
        TransferRequest request = new TransferRequest("1234", "5678", BigDecimal.valueOf(200));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByNumberAndUserIdAndActiveIsTrue("1234", user.getId())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByNumber("5678")).thenReturn(Optional.of(toAccount));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                accountService.transferToAccount(authentication, request, Locale.ENGLISH));

        assertEquals("deactive-account", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void test_TransferToAccount_InsufficientBalance() {
        TransferRequest request = new TransferRequest("1234", "5678", BigDecimal.valueOf(2000));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findByNumberAndUserIdAndActiveIsTrue("1234", user.getId())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByNumber("5678")).thenReturn(Optional.of(toAccount));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                accountService.transferToAccount(authentication, request, Locale.ENGLISH));

        assertEquals("insufficient-balance", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

}