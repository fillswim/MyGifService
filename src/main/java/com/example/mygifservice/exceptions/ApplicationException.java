package com.example.mygifservice.exceptions;

import lombok.Getter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ApplicationException extends ResponseStatusException {

    private final ErrorAttributeOptions options;

    public ApplicationException(HttpStatus httpStatus, String message, ErrorAttributeOptions options) {
        super(httpStatus, message);
        this.options = options;
    }


}
