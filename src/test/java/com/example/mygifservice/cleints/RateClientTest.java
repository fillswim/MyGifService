package com.example.mygifservice.cleints;

import com.example.mygifservice.AbstractTest;
import com.example.mygifservice.clients.RateClient;
import com.example.mygifservice.exceptions.RatesNotFoundException;
import com.example.mygifservice.models.RatesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class RateClientTest extends AbstractTest {

    @Value("${oxr.app.id}")
    private String foerAppId;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @Value("${test.date}")
    private String dateYesterday;

    @Value("${test.rate.latest}")
    private BigDecimal latestRateExp;

    @Value("${test.rate.historical}")
    private BigDecimal historicalRateExp;

    @MockBean
    private RateClient rateClient;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {

        Optional<RatesResponse> latestRatesResponse = ratesJsonFromResourcesToOptional("static/latest.json");
        Optional<RatesResponse> historicalRatesResponse = ratesJsonFromResourcesToOptional("static/historical.json");

        Mockito.when(rateClient.getResponseRates(foerAppId))
                .thenReturn(latestRatesResponse);

        Mockito.when(rateClient.getResponseRates(dateYesterday, foerAppId))
                .thenReturn(historicalRatesResponse);
    }

    @Test
    void getLatestRates() {

        RatesResponse latestRatesResponse = rateClient.getResponseRates(foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("The latest exchange rates were not found"));

        BigDecimal latestRate = latestRatesResponse.getRates().get(quotedCurrency);

        assertNotNull(latestRatesResponse);
        assertEquals(latestRateExp, latestRate);
    }

    @Test
    void getHistoricalRates() {

        RatesResponse historicalRatesResponse = rateClient.getResponseRates(dateYesterday, foerAppId)
                .orElseThrow(() -> new RatesNotFoundException("Historical exchange rates for " + dateYesterday + " were not found"));

        BigDecimal historicalRate = historicalRatesResponse.getRates().get(quotedCurrency);

        assertNotNull(historicalRatesResponse);
        assertEquals(historicalRateExp, historicalRate);
    }
}