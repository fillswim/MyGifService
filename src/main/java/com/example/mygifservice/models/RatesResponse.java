package com.example.mygifservice.models;

import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RatesResponse {

    String disclaimer;
    String license;
    Long timestamp;
    String base;
    Map<String, Double> rates;

}
