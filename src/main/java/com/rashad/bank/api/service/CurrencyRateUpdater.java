package com.rashad.bank.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRateUpdater {

    private final CurrencyRateService currencyRateService;

    @Scheduled(fixedRate = 60000)
    public void updateRates() {
        currencyRateService.updateRates();
    }
}
