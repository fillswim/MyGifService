package com.example.mygifservice.cleints;

import com.example.mygifservice.AbstractTest;
import com.example.mygifservice.clients.GiphyClient;
import com.example.mygifservice.exceptions.GifsNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class GiphyClientTest extends AbstractTest {

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${giphy.tag.zero}")
    private String giphyTagZero;

    @Value("${giphy.app.id}")
    private String giphyAppId;

    @MockBean
    private GiphyClient giphyClient;

    @BeforeEach
    public void init() throws URISyntaxException, IOException {

        Optional<String> giphyJsonString = Optional.of(jsonFromResourcesToString("static/giphyResponseBroke.json"));

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagBroke))
                .thenReturn(giphyJsonString);
    }

    @Test
    void getGif() {

        String giphyJsonString = giphyClient.getGif(giphyAppId, giphyTagBroke)
                .orElseThrow(() -> new GifsNotFoundException("Gifs with the tag " + giphyTagBroke + " were not found"));

        JsonObject jsonObject = JsonParser.parseString(giphyJsonString).getAsJsonObject();

        String type = jsonObject
                .getAsJsonObject().get("data")
                .getAsJsonObject().get("type")
                .getAsString();

        assertEquals("gif", type);

    }
}