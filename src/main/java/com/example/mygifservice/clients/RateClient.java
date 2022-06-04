package com.example.mygifservice.clients;

import com.example.mygifservice.models.RatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "RateClient", url = "${oxr.url.general}")
public interface RateClient {

    @GetMapping("/latest.json")
    Optional<RatesResponse> getResponseRates(
            @RequestParam("app_id") String appId);

    @GetMapping("/historical/{date}.json")
    Optional<RatesResponse> getResponseRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId);


}
