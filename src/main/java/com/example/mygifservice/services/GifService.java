package com.example.mygifservice.services;

import com.example.mygifservice.clients.GiphyClient;
import com.example.mygifservice.exceptions.GifsNotFoundException;
import com.example.mygifservice.models.ProfitStatus;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GifService {

    private final GiphyClient giphyClient;
    private final RateService rateService;
    private final LinkService linkService;

    @Value("${giphy.app.id}")
    private String giphyAppId;

    @Value("${giphy.tag.reach}")
    private String giphyTagReach;

    @Value("${giphy.tag.broke}")
    private String giphyTagBroke;

    @Value("${giphy.tag.zero}")
    private String giphyTagZero;

    public GifService(GiphyClient giphyClient, RateService rateService, LinkService linkService) {
        this.giphyClient = giphyClient;
        this.rateService = rateService;
        this.linkService = linkService;
    }

    public byte[] getGif(String currencyCode) {

        ProfitStatus profitStatus = rateService.getRateStatus(currencyCode);

        String giphyServiceResponse = null;

        switch (profitStatus) {
            case REACH -> giphyServiceResponse = loadResponseFromGiphy(giphyTagReach);
            case BROKE -> giphyServiceResponse = loadResponseFromGiphy(giphyTagBroke);
            case ZERO -> giphyServiceResponse = loadResponseFromGiphy(giphyTagZero);
        }

        String link = linkService.getLink(giphyServiceResponse);

        return getGifBytes(link);
    }

    private String loadResponseFromGiphy(@NonNull String gifTag) {

        return giphyClient.getGif(giphyAppId, gifTag)
                .orElseThrow(() -> new GifsNotFoundException("Gifs with the tag " + gifTag + " were not found"));
    }

    private byte[] getGifBytes(@NonNull String link) {

        byte[] array = new byte[0];

        try {
            array = IOUtils.toByteArray(new URL(link));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return array;
    }

}
