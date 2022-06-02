package com.example.mygifservice.services;

import com.example.mygifservice.clients.GiphyClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

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
        //FIXME Validation  Objects.requireNonNull(currencyCode, "currencyCode is required");  or Lombok NotNull.

        // Get profit status //TODO You don't need to comment each line. :)
        String profitStatus = foerService.getRateStatus(currencyCode);

        // Get answer from api.giphy.com
        String giphyResponse = loadResponseFromGiphy(profitStatus); //FIXME You can do it without the method.
        //TODO Why can't it return a serialized object?

        // Get download link
        String link = linkService.getLink(giphyResponse); //TODO I would merge loadResponseFromGiphy with getLink.

        // Download gif
        byte[] gifBytes = getGifBytes(link); //FIXME No need for the variable.

        return gifBytes;
    }

    private String loadResponseFromGiphy(String status) { //FIXME Input Validation

        String giphyResponse = giphyClient.getGif(giphyAppId, status); //FIXME Error handling

        return giphyResponse; //FIXME You don't the variable here
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
