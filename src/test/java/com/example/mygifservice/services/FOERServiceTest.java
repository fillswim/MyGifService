package com.example.mygifservice.services;

import com.example.mygifservice.clients.FOERClient;
import com.example.mygifservice.models.Rates;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class FOERServiceTest {

    @Value("${oxr.app.id}")
    private String foerAppId;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @Value("${test.date}")
    private String dateYesterday;

    @MockBean
    private FOERClient foerClient;

    @Autowired
    private FOERService foerService;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {

        String latestJsonName = "static/latest.json";
        Path latestPath = Paths.get(getClass().getResource("/" + latestJsonName).toURI());
        String latestJsonString = Files.readString(latestPath);

        String historicalJsonName = "static/historical.json";
        Path historicalPath = Paths.get(getClass().getResource("/" + historicalJsonName).toURI());
        String historicalJsonString = Files.readString(historicalPath);

        Gson gson = new Gson();
        Rates latestRates = gson.fromJson(latestJsonString, Rates.class);
        Rates historicalRates = gson.fromJson(historicalJsonString, Rates.class);

        Mockito.when(foerClient.getLatestRates(foerAppId))
                .thenReturn(latestRates);

        Mockito.when(foerClient.getHistoricalRates(dateYesterday, foerAppId))
                .thenReturn(historicalRates);
    }

    @Test
    void getRateStatus() {

        String profitStatus = foerService.getRateStatus(quotedCurrency);

        assertEquals(giphyTagBroke, profitStatus);
    }
}