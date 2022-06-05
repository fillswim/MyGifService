package com.example.mygifservice.controllers;

import com.example.mygifservice.services.GifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
class GifControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Value("${currency.quoted}")
    private String quotedCurrency;

    @MockBean
    private GifService gifService;

    @BeforeEach
    public void init() throws IOException {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Resource fileResource = new ClassPathResource("static/giphyBroke.gif");
        byte[] bytes = fileResource.getInputStream().readAllBytes();

        assertNotNull(bytes);

        Mockito.when(gifService.getGif(quotedCurrency))
                .thenReturn(bytes);
    }

    @Test
    void getGif_Ok() throws Exception {

        Resource fileResource = new ClassPathResource("static/giphyBroke.gif");

        mockMvc.perform(get("/api/gifs/" + quotedCurrency))
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileResource.getInputStream().readAllBytes()));
    }

    @Test
    void getGif_NotFound() throws Exception {

        mockMvc.perform(get("/api/gifs"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getGif_BadRequest() throws Exception {

        mockMvc.perform(get("/api/gifs/Rub"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}