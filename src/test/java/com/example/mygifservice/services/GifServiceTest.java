package com.example.mygifservice.services;

import com.example.mygifservice.AbstractTest;
import com.example.mygifservice.client.GiphyClient;
import com.example.mygifservice.exceptions.GifsNotFoundException;
import com.example.mygifservice.models.ProfitStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GifServiceTest extends AbstractTest {

    @Value("${giphy.app.id}")
    private String giphyAppId;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${giphy.tag.rich}")
    private String giphyTagRich;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @MockBean
    private RateService rateService;

    @MockBean // FIXME SB нужны веские основания для такой аннотации. Лучше стандартные средства mockito использовать
    private GiphyClient giphyClient;

    @Autowired
    private GifService gifService;

    @Test
    void getGif_Ok_Broke() throws IOException, URISyntaxException {

        // FOERService answer
        Mockito.when(rateService.getRateStatus(quotedCurrency))
                .thenReturn(ProfitStatus.BROKE);

        // GiphyClient answer
        Optional<String> giphyJsonString = Optional.ofNullable(jsonFromResourcesToString("static/giphyResponseBroke.json"));

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagBroke))
                .thenReturn(giphyJsonString);

        byte[] downloadByteArray = gifFromResourcesToByteArray("static/giphyBroke.gif");

        byte[] testByteArray = gifService.getGif(quotedCurrency);

        assertNotNull(testByteArray);
        assertEquals(downloadByteArray.length, testByteArray.length);
    }

    @Test
    void getGif_Ok_Rich() throws IOException, URISyntaxException {

        // FOERService answer
        Mockito.when(rateService.getRateStatus(quotedCurrency))
                .thenReturn(ProfitStatus.RICH);

        // GiphyClient answer
        Optional<String> giphyJsonString = Optional.ofNullable(jsonFromResourcesToString("static/giphyResponseRich.json"));

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagRich))
                .thenReturn(giphyJsonString);


        byte[] downloadByteArray = gifFromResourcesToByteArray("static/giphyRich.gif");

        byte[] testByteArray = gifService.getGif(quotedCurrency);

        assertNotNull(testByteArray);
        assertEquals(downloadByteArray.length, testByteArray.length);
    }

    @Test
    void getGif_Exception() {

        // FOERService answer
        Mockito.when(rateService.getRateStatus(quotedCurrency))
                .thenReturn(ProfitStatus.BROKE);

        // GiphyClient answer
        Optional<String> giphyJsonString = Optional.ofNullable(null);

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagBroke))
                .thenReturn(giphyJsonString);


        GifsNotFoundException exception = assertThrows(GifsNotFoundException.class, () -> gifService.getGif(quotedCurrency));
        assertNotNull(exception.getMessage());
    }

    @Test
    void getGif_Null() {

        String currencyCode = null;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> gifService.getGif(currencyCode));
        assertEquals("currencyCode is marked non-null but is null", exception.getMessage());

    }
}