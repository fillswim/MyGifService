package com.example.mygifservice.services;

import com.example.mygifservice.clients.FOERClient;
import com.example.mygifservice.exceptions.CurrencyNotFoundException;
import com.example.mygifservice.models.Rates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class FOERService {

    private final FOERClient foerClient;

    @Value("${oxr.app.id}")
    private String foerAppId;

    @Value("${giphy.tag.reach}") //TODO  Why? Just create ENUM
    private String giphyTagReach;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${giphy.tag.zero}")
    private String giphyTagZero;

    public FOERService(FOERClient foerClient) {
        this.foerClient = foerClient;
    }


    public String getRateStatus(String currencyCode) { //FIXME Validation

        LocalDate localDate = LocalDate.now().minusDays(1); //FIXME Move 1 to the constant

        Rates historicalRates = foerClient.getHistoricalRates(localDate.toString(), foerAppId); //FIXME Exception handling. What if the service returned null?

        Double historicalRate = historicalRates.getRates().get(currencyCode); //FIXME Check that rates are not null

        Rates latestRates = foerClient.getLatestRates(foerAppId); //FIXME Exception handling.
        Double latestRate = latestRates.getRates().get(currencyCode); //FIXME Check that rates are not null

        if (historicalRate == null || latestRate == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }

        log.info("IN getRateStatus() - historicalRate: {}, latestRate: {}", historicalRate, latestRate); //TODO Better to use Debug

        Double difference = latestRate - historicalRate; //TODO Why Double, not double?

        log.info("IN getRateStatus() - difference: {}", difference);

        String profitStatus;

        if (difference > 0) { //FIXMe Move it to a separate method, so you don't need to have the variable.
            profitStatus = giphyTagReach;
        } else if (difference < 0) {
            profitStatus = giphyTagBroke;
        } else {
            profitStatus = giphyTagZero;
        }

        log.info("IN getRateStatus() - profitStatus: {}", profitStatus);

        return profitStatus;
    }

}
