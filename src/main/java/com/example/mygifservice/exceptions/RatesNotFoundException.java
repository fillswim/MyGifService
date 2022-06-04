package com.example.mygifservice.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class RatesNotFoundException extends ApplicationException {
    public RatesNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
