package com.example.mygifservice.services;

import com.example.mygifservice.client.GiphyClient;
import com.example.mygifservice.exceptions.GifDownloadException;
import com.example.mygifservice.exceptions.GifsNotFoundException;
import com.example.mygifservice.models.ProfitStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class GifService {

    private final GiphyClient giphyClient;
    private final RateService rateService;
    private final LinkService linkService;

    @Value("${giphy.app.id}")
    private String giphyAppId;

    public byte[] getGif(@NonNull String currencyCode) {

        final ProfitStatus profitStatus = rateService.getRateStatus(currencyCode);

        String giphyTag = null;

        switch (profitStatus) {
            case BROKE:
                giphyTag = "broke";
                break;
            case RICH:
                giphyTag = "rich";
                break;
            case ZERO:
                giphyTag = "zero";
                break;
        }

        String giphyServiceResponse = loadResponseFromGiphy(giphyTag);

        String link = linkService.getLink(giphyServiceResponse);

        return getGifBytes(link);
    }

    private String loadResponseFromGiphy(@NonNull String gifTag) {

        return giphyClient.getGif(giphyAppId, gifTag)
                .orElseThrow(() -> new GifsNotFoundException("Gifs with the tag " + gifTag + " were not found"));
    }

    private byte[] getGifBytes(@NonNull String link) {

        byte[] array;

        try {
            array = IOUtils.toByteArray(new URL(link));
        } catch (IOException exception) {
            throw new GifDownloadException(exception.getMessage());
        }

        return array;
    }

}
