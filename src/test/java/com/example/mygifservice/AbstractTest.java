package com.example.mygifservice;

import com.example.mygifservice.models.RatesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class AbstractTest {

    public String jsonFromResourcesToString(String filename) throws URISyntaxException, IOException {

        Path path = Paths.get(getClass().getResource("/" + filename).toURI());
        String string = Files.readString(path);

        return string;
    }

    public Optional<RatesResponse> ratesJsonFromResourcesToOptional(String filename) throws IOException, URISyntaxException {

        String readString = jsonFromResourcesToString(filename);

        Gson gson = new Gson();
        Optional<RatesResponse> optional = Optional.ofNullable(gson.fromJson(readString, RatesResponse.class));

        return optional;
    }

    public byte[] gifFromResourcesToByteArray(String filename) throws URISyntaxException, IOException {

        Path gifPath = Paths.get(getClass().getResource("/" + filename).toURI());
        byte[] downloadByteArray = Files.readAllBytes(gifPath);

        return downloadByteArray;
    }
}
