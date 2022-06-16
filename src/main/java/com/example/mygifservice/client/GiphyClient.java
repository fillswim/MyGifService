package com.example.mygifservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "GiphyClient", url = "${giphy.url.general}")
public interface GiphyClient {

    @GetMapping("/random")
    Optional<String> getGif(
            @RequestParam("api_key") String appId,
            @RequestParam("tag") String tag);

}
