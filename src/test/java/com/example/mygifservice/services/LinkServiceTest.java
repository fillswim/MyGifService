package com.example.mygifservice.services;

import com.example.mygifservice.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class LinkServiceTest extends AbstractTest {

    @Value("${test.giphy.url.right}")
    private String rightUrl;

    LinkService linkService = new LinkService();

    @Test
    void getLink() throws URISyntaxException, IOException {

        String giphyJsonString = jsonFromResourcesToString("static/giphyResponseBroke.json");

        assertEquals(rightUrl, linkService.getLink(giphyJsonString));

    }

    @Test
    void getLink_Null() {

        String jsonString = null;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> linkService.getLink(jsonString));
        assertEquals("jsonString is marked non-null but is null", exception.getMessage());
    }
}