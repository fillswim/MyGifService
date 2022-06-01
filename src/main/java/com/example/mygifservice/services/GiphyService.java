package com.example.mygifservice.services;

import com.example.mygifservice.clients.GiphyClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GiphyService {

    private final GiphyClient giphyClient;

    private final FOERService foerService;

    private final LinkService linkService;

    @Value("${giphy.app.id}")
    private String giphyAppId;

    public GiphyService(GiphyClient giphyClient, FOERService foerService, LinkService linkService) {
        this.giphyClient = giphyClient;
        this.foerService = foerService;
        this.linkService = linkService;
    }

    public byte[] getGif(String currencyCode) {

        // Get profit status
        String profitStatus = foerService.getRateStatus(currencyCode);

        // Get answer from api.giphy.com
        String giphyResponse = loadResponseFromGiphy(profitStatus);

        // Get download link
        String link = linkService.getLink(giphyResponse);

        // Download gif
        byte[] gifBytes = getGifBytes(link);

        return gifBytes;
    }

    private String loadResponseFromGiphy(String status) {

        String giphyResponse = giphyClient.getGif(giphyAppId, status);

        return giphyResponse;
    }

    private byte[] getGifBytes(String link) {

        byte[] array = new byte[0];

        try {
            array = IOUtils.toByteArray(new URL(link));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return array;
    }

}
