package com.example.mygifservice.services;

import com.example.mygifservice.AbstractTest;
import com.example.mygifservice.client.RateClient;
import com.example.mygifservice.exceptions.RatesNotFoundException;
import com.example.mygifservice.models.ProfitStatus;
import com.example.mygifservice.models.RatesResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class RateServiceTest extends AbstractTest {

    @Value("${oxr.app.id}")
    private String foerAppId;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @Value("${app.day.daysbefore}")
    private int daysBefore;

    @MockBean
    private RateClient rateClient;

    @Autowired
    private RateService rateService;

    @Test
    void getRateStatus_Ok() throws URISyntaxException, IOException {

        LocalDate localDate = LocalDate.now().minusDays(daysBefore);

        Optional<RatesResponse> latestRatesResponse = ratesJsonFromResourcesToOptional("static/latest.json");
        Optional<RatesResponse> historicalRatesResponse = ratesJsonFromResourcesToOptional("static/historical.json");

        Mockito.when(rateClient.getResponseRates(foerAppId))
                .thenReturn(latestRatesResponse);

        Mockito.when(rateClient.getResponseRates(localDate.toString(), foerAppId))
                .thenReturn(historicalRatesResponse);

        ProfitStatus profitStatus = rateService.getRateStatus(quotedCurrency);

        assertEquals(ProfitStatus.BROKE, profitStatus);
    }

    @Test
    void getRateStatus_Exception() {

        LocalDate localDate = LocalDate.now().minusDays(daysBefore);

        Optional<RatesResponse> latestRatesResponse = Optional.ofNullable(null);
        Optional<RatesResponse> historicalRatesResponse = Optional.ofNullable(null);

        Mockito.when(rateClient.getResponseRates(foerAppId))
                .thenReturn(latestRatesResponse);

        Mockito.when(rateClient.getResponseRates(localDate.toString(), foerAppId))
                .thenReturn(historicalRatesResponse);


        RatesNotFoundException exception = assertThrows(RatesNotFoundException.class, () -> rateService.getRateStatus(quotedCurrency));
        assertNotNull(exception.getMessage());
    }

    @Test
    void getRateStatus_NonNull() {

        String currencyCode = null;

        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> rateService.getRateStatus(currencyCode));
        assertEquals("currencyCode is marked non-null but is null", nullPointerException.getMessage());

    }

}