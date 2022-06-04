package com.example.mygifservice.resource.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// Тело ответа сервера
@Data
@AllArgsConstructor
public class MessageResponse {

    private String message;

}
