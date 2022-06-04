package com.example.mygifservice.controllers;

import com.example.mygifservice.services.GifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class GifControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @MockBean
    private GifService gifService;



    @BeforeEach
    public void init() throws IOException {

        Resource fileResource = new ClassPathResource("static/giphy.gif");
        byte[] bytes = fileResource.getInputStream().readAllBytes();

        assertNotNull(bytes);

        Mockito.when(gifService.getGif(quotedCurrency))
                .thenReturn(bytes);
    }

    @Test
    void getGif() throws Exception {

        Resource fileResource = new ClassPathResource("static/giphy.gif");

        mockMvc.perform(get("/api/gifs/" + quotedCurrency))
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileResource.getInputStream().readAllBytes()));
    }
}