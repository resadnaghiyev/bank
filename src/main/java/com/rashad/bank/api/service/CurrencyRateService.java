package com.rashad.bank.api.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CurrencyRateService {

    private final Map<String, Double> rates = new ConcurrentHashMap<>();

    public CurrencyRateService() {
        rates.put("USD/AZN", 1.7);
        rates.put("EUR/AZN", 1.8);
        rates.put("AZN/TL", 19.0);
        rates.put("AZN/RUB", 51.0);
    }

    public void updateRates() {
        rates.put("USD/AZN", rates.get("USD/AZN") + (Math.random() - 0.5));
        rates.put("EUR/AZN", rates.get("EUR/AZN") + (Math.random() - 0.5));
        rates.put("AZN/TL", rates.get("AZN/TL") + (Math.random() - 0.5));
        rates.put("AZN/RUB", rates.get("AZN/RUB") + (Math.random() - 0.5));
    }

    public Double getRateByPair(String currencyPair) {
        Double rate = rates.getOrDefault(currencyPair, 0.0);
        BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Map<String, Double> getAllRates() {
        return rates.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry ->
                        BigDecimal.valueOf(entry.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
    }
}
