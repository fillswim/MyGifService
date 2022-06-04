package com.example.mygifservice.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LinkService {

    public String getLink(@NonNull String jsonString) {

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        String wrongLink = jsonObject
                .getAsJsonObject().get("data")
                .getAsJsonObject().get("images")
                .getAsJsonObject().get("original")
                .getAsJsonObject().get("url")
                .getAsString();

        return replaceLink(wrongLink);
    }

    private String replaceLink(@NonNull String wrongLink) {

        Pattern pattern = Pattern.compile("media\\d.giphy.com");
        Matcher matcher = pattern.matcher(wrongLink);

        String rightLink = matcher.replaceFirst("i.giphy.com");

        return rightLink;
    }

}
