package com.example.mygifservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GiphyClient", url = "${giphy.url.general}")
public interface GiphyClient {

    @GetMapping("/random")
    String getGif(
            @RequestParam("api_key") String appId,
            @RequestParam("tag") String tag);

}
