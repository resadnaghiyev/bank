package com.rashad.bank.api.service;

import com.rashad.bank.api.dto.request.AddAccountRequest;
import com.rashad.bank.api.dto.request.TransferRequest;
import com.rashad.bank.api.dto.response.CustomResponse;
import org.springframework.security.core.Authentication;

import java.util.Locale;

public interface AccountService {

    CustomResponse getAccounts(Authentication auth, Locale locale);

    CustomResponse addAccount(Authentication auth, AddAccountRequest request, Locale locale);

    CustomResponse deActivateAccount(Authentication auth, String number, Locale locale);

    CustomResponse transferToAccount(Authentication auth, TransferRequest request, Locale locale);
}
