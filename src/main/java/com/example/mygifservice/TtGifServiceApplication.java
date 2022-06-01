package com.example.mygifservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TtGifServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtGifServiceApplication.class, args);
    }

}
