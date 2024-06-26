package com.rashad.bank.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyRateServiceTest {

    private CurrencyRateService currencyRateService;

    @BeforeEach
    void setUp() {
        currencyRateService = new CurrencyRateService();
    }

    @Test
    void test_UpdateRates() {
        Map<String, Double> initialRates = currencyRateService.getAllRates();

        currencyRateService.updateRates();
        Map<String, Double> updatedRates = currencyRateService.getAllRates();

        assertNotNull(updatedRates);
        assertEquals(4, updatedRates.size());
        for (String key : initialRates.keySet()) {
            assertNotEquals(initialRates.get(key), updatedRates.get(key), "Rate for " + key + " should be updated.");
        }
    }

    @Test
    void test_GetRateByPair() {
        String currencyPair = "USD/AZN";
        Double expectedRate = 1.70;

        Double actualRate = currencyRateService.getRateByPair(currencyPair);

        assertNotNull(actualRate);
        assertEquals(expectedRate, actualRate, "Rate for " + currencyPair + " should be " + expectedRate);
    }

    @Test
    void test_GetRateByPair_InvalidPair() {
        String invalidCurrencyPair = "USD/XYZ";

        Double actualRate = currencyRateService.getRateByPair(invalidCurrencyPair);

        assertNotNull(actualRate);
        assertEquals(0.0, actualRate, "Rate for invalid currency pair should be 0.0");
    }

    @Test
    void testGetAllRates() {
        Map<String, Double> rates = currencyRateService.getAllRates();

        assertNotNull(rates);
        assertEquals(4, rates.size());
        assertTrue(rates.containsKey("USD/AZN"));
        assertTrue(rates.containsKey("EUR/AZN"));
        assertTrue(rates.containsKey("AZN/TL"));
        assertTrue(rates.containsKey("AZN/RUB"));
    }


}