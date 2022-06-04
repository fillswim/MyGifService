package com.example.mygifservice.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class GifsNotFoundException extends ApplicationException {
    public GifsNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
