package com.example.mygifservice.controllers;

import com.example.mygifservice.services.GifService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/gifs")
@Api(tags = "Gif Controller")
@Validated
public class GifController {
    private final GifService gifService;

    public GifController(GifService gifService) {
        this.gifService = gifService;
    }

    @ApiOperation(value = "Get gif by currency code")
    @GetMapping(value = "/{currencyCode}", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity<byte[]> getGif(
            @Pattern(regexp = "^[A-Z]{3}$", message = "The format of the currency code must be ХХХ")
            @PathVariable("currencyCode") String currencyCode) {

        byte[] array = gifService.getGif(currencyCode);;

        ResponseEntity<byte[]> responseEntity = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(array);

        return responseEntity;
    }

}
