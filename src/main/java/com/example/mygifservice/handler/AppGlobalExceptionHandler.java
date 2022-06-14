package com.example.mygifservice.handler;

import com.example.mygifservice.exceptions.ApplicationException;
import com.example.mygifservice.resource.response.MessageResponse;
import com.google.gson.Gson;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AppGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> appException(ApplicationException exception, WebRequest request) {

        log.error("Application Exception", exception);

        Map<String, Object> body = errorAttributes.getErrorAttributes(request, exception.getOptions());
        HttpStatus status = exception.getStatus();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler()
    public ResponseEntity<Map> handleFeignStatusException(FeignException exception) {

        log.error("FeignException has been thrown");

        Gson gson = new Gson();
        Map map = gson.fromJson(exception.contentUTF8(), Map.class);

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> appException(ConstraintViolationException exception) {

        log.error("ConstraintViolationException has been thrown");

        String message = exception.getMessage();

        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
    }

}
