package com.example.mygifservice.cleints;

import com.example.mygifservice.clients.GiphyClient;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class GiphyClientTest {

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

        String giphyJsonName = "static/giphyResponse.json";
        Path giphyPath = Paths.get(getClass().getResource("/" + giphyJsonName).toURI());
        Optional<String> giphyJsonString = Optional.of(Files.readString(giphyPath));

        Mockito.when(giphyClient.getGif(giphyAppId, giphyTagBroke))
                .thenReturn(giphyJsonString);
    }

    @Test
    void getGif() {

        String giphyJsonString = giphyClient.getGif(giphyAppId, giphyTagBroke).get();

        JsonObject jsonObject = JsonParser.parseString(giphyJsonString).getAsJsonObject();

        String type = jsonObject
                .getAsJsonObject().get("data")
                .getAsJsonObject().get("type")
                .getAsString();

        assertEquals("gif", type);

    }
}