package com.example.mygifservice.models;

import lombok.*;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rates {

    String disclaimer;
    String license;
    String timestamp;
    String base;
    Map<String, Double> rates;

}
