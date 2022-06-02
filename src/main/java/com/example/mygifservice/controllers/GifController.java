package com.example.mygifservice.controllers;

import com.example.mygifservice.services.GiphyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gifs")
@Api(tags = "Gif Controller")
public class GifController {
    private final GiphyService giphyService;

    public GifController(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    @ApiOperation(value = "Get gif by currency code")
    @GetMapping(value = "/{currencyCode}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity<byte[]> getGif(@PathVariable("currencyCode") String currencyCode) { //FIXME Validation (can't be black, format)

        byte[] array = giphyService.getGif(currencyCode);

        ResponseEntity<byte[]> responseEntity = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(array);

        return responseEntity;
    }

}
