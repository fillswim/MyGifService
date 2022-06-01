package com.example.mygifservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class LinkServiceTest {

    @Value("${test.giphy.url.right}")
    private String rightUrl;

    LinkService linkService = new LinkService();

    @Test
    void getLink() throws URISyntaxException, IOException {

        String giphyJsonName = "static/giphyResponse.json";
        Path giphyPath = Paths.get(getClass().getResource("/" + giphyJsonName).toURI());
        String giphyJsonString = Files.readString(giphyPath);

        assertEquals(rightUrl, linkService.getLink(giphyJsonString));

    }
}