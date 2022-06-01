package com.example.mygifservice.clients;

import com.example.mygifservice.models.Rates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "FOERClient", url = "${oxr.url.general}")
public interface FOERClient {

    @GetMapping("/latest.json")
    Rates getLatestRates(@RequestParam("app_id") String appId);


    @GetMapping("/historical/{date}.json")
    Rates getHistoricalRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId);



}
