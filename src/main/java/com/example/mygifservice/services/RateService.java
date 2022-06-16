package com.example.mygifservice.services;

import com.example.mygifservice.clients.RateClient;
import com.example.mygifservice.exceptions.CurrencyNotFoundException;
import com.example.mygifservice.exceptions.RatesNotFoundException;
import com.example.mygifservice.models.ProfitStatus;
import com.example.mygifservice.models.RatesResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Service
@Slf4j
public class RateService {

    private static final int NUMBER_OF_DAYS_AGO = 1;

    private final RateClient rateClient;

    @Value("${oxr.app.id}")
    private String foerAppId;

    public RateService(RateClient rateClient) {
        this.rateClient = rateClient;
    }


    public ProfitStatus getRateStatus(@NonNull String currencyCode) {

        LocalDate localDate = LocalDate.now().minusDays(NUMBER_OF_DAYS_AGO);


        RatesResponse historicalRatesResponse = rateClient.getResponseRates(localDate.toString(), foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("Historical exchange rates for " + localDate + " were not found"));

        BigDecimal historicalRate = getRateFromRates(historicalRatesResponse, currencyCode);


        RatesResponse latestRatesResponse = rateClient.getResponseRates(foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("The latest exchange rates were not found"));

        BigDecimal latestRate = getRateFromRates(latestRatesResponse, currencyCode);

        if (latestRate.compareTo(historicalRate) > 0) {
            return ProfitStatus.RICH;
        } else if (latestRate.compareTo(historicalRate) < 0) {
            return ProfitStatus.BROKE;
        } else {
            return ProfitStatus.ZERO;
        }
    }

    private BigDecimal getRateFromRates(RatesResponse ratesResponse, String currencyCode) {

        Map<String, BigDecimal> rates = ratesResponse.getRates();

        Instant instant = Instant.ofEpochSecond(ratesResponse.getTimestamp());
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of("Europe/Moscow"));

        BigDecimal rate;

        if (ratesResponse.getRates().containsKey(currencyCode)) {
            rate = rates.get(currencyCode);
        } else {
            throw new CurrencyNotFoundException(
                    "There is no last quotation for the currency code " + currencyCode + " on " + localDate);
        }

        return rate;
    }

}
