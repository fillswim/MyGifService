package com.example.mygifservice.services;

import com.example.mygifservice.clients.FOERClient;
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

    @Value("${giphy.tag.reach}")
    private String giphyTagReach;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${giphy.tag.zero}")
    private String giphyTagZero;

    public FOERService(FOERClient foerClient) {
        this.foerClient = foerClient;
    }


    public String getRateStatus(String currencyCode) {

        LocalDate localDate = LocalDate.now().minusDays(1);

        Rates historicalRates = foerClient.getHistoricalRates(localDate.toString(), foerAppId);
        Double historicalRate = historicalRates.getRates().get(currencyCode);


        Rates latestRates = foerClient.getLatestRates(foerAppId);
        Double latestRate = latestRates.getRates().get(currencyCode);

        log.info("IN getRateStatus() - historicalRate: {}, latestRate: {}", historicalRate, latestRate);

        Double difference = latestRate - historicalRate;

        log.info("IN getRateStatus() - difference: {}", difference);

        String profitStatus;

        if (difference > 0) {
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
