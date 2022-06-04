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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Service
@Slf4j
public class RateService {

    private final RateClient rateClient;

    @Value("${app.day.daysbefore}")
    private int daysBefore;

    @Value("${oxr.app.id}")
    private String foerAppId;

    public RateService(RateClient rateClient) {
        this.rateClient = rateClient;
    }


    public ProfitStatus getRateStatus(@NonNull String currencyCode) {

        LocalDate localDate = LocalDate.now().minusDays(daysBefore);


        RatesResponse historicalRatesResponse = rateClient.getResponseRates(localDate.toString(), foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("Historical exchange rates for " + localDate + " were not found"));

        double historicalRate = getRateFromRates(historicalRatesResponse, currencyCode);


        RatesResponse latestRatesResponse = rateClient.getResponseRates(foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("The latest exchange rates were not found"));

        double latestRate = getRateFromRates(latestRatesResponse, currencyCode);

        log.info("IN getRateStatus() - historicalRate: {}, latestRate: {}", historicalRate, latestRate);

        if (latestRate > historicalRate) {
            return ProfitStatus.REACH;
        } else if (latestRate < historicalRate) {
            return ProfitStatus.BROKE;
        } else {
            return ProfitStatus.ZERO;
        }
    }

    private double getRateFromRates(RatesResponse ratesResponse, String currencyCode) {

        Map<String, Double> rates = ratesResponse.getRates();

        Instant instant = Instant.ofEpochSecond(ratesResponse.getTimestamp());
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of("Europe/Moscow"));

        double rate;

        if (ratesResponse.getRates().containsKey(currencyCode)) {
            rate = rates.get(currencyCode);
        } else {
            throw new CurrencyNotFoundException(
                    "There is no last quotation for the currency code " + currencyCode + " on " + localDate);
        }

        return rate;
    }

}
