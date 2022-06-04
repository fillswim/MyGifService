package com.example.mygifservice.services;

import com.example.mygifservice.clients.RateClient;
import com.example.mygifservice.models.ProfitStatus;
import com.example.mygifservice.models.RatesResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class RateServiceTest {

    @Value("${oxr.app.id}")
    private String foerAppId;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @Value("${test.date}")
    private String dateYesterday;

    @MockBean
    private RateClient rateClient;

    @Autowired
    private RateService rateService;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {

        String latestJsonName = "static/latest.json";
        Path latestPath = Paths.get(getClass().getResource("/" + latestJsonName).toURI());
        String latestJsonString = Files.readString(latestPath);

        String historicalJsonName = "static/historical.json";
        Path historicalPath = Paths.get(getClass().getResource("/" + historicalJsonName).toURI());
        String historicalJsonString = Files.readString(historicalPath);

        Gson gson = new Gson();
        Optional<RatesResponse> latestRatesResponse = Optional.of(gson.fromJson(latestJsonString, RatesResponse.class));
        Optional<RatesResponse> historicalRatesResponse = Optional.of(gson.fromJson(historicalJsonString, RatesResponse.class));

        Mockito.when(rateClient.getResponseRates(foerAppId))
                .thenReturn(latestRatesResponse);

        Mockito.when(rateClient.getResponseRates(dateYesterday, foerAppId))
                .thenReturn(historicalRatesResponse);
    }

    @Test
    void getRateStatus() {

        ProfitStatus profitStatus = rateService.getRateStatus(quotedCurrency);

        assertEquals(ProfitStatus.BROKE, profitStatus);
    }
}