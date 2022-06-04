package com.example.mygifservice.services;

import com.example.mygifservice.clients.GiphyClient;
import com.example.mygifservice.models.ProfitStatus;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class GifServiceTest {

    @Value("${giphy.app.id}")
    private String giphyAppId;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @Value("${test.giphy.url.right}")
    private String rightUrl;

    @MockBean
    private RateService rateService;

    @MockBean
    private GiphyClient giphyClient;

    @MockBean
    private LinkService linkService;

    @Autowired
    private GifService gifService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {

        // FOERService answer
        Mockito.when(rateService.getRateStatus(quotedCurrency))
                .thenReturn(ProfitStatus.BROKE);

        // GiphyClient answer
        String giphyJsonName = "static/giphyResponse.json";
        Path giphyPath = Paths.get(getClass().getResource("/" + giphyJsonName).toURI());
        Optional<String> giphyJsonString = Optional.of(Files.readString(giphyPath));

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagBroke))
                .thenReturn(giphyJsonString);


        // LinkService answer
        Mockito.when(linkService.getLink(giphyJsonString.get()))
                .thenReturn(rightUrl);

    }


    @Test
    void getGif() throws IOException, URISyntaxException {

        String gifName = "static/giphy.gif";
        Path gifPath = Paths.get(getClass().getResource("/" + gifName).toURI());
        byte[] downloadByteArray = Files.readAllBytes(gifPath);

        byte[] testByteArray = gifService.getGif(quotedCurrency);

        assertNotNull(testByteArray);
        assertEquals(downloadByteArray.length, testByteArray.length);
    }
}