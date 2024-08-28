package com.example.tires.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;


@Data
@Service
public class ConfigurationLoader {
    @Value("classpath:configuration.json")
    private org.springframework.core.io.Resource resourceFile;

    private JsonNode jsonArray;

    @PostConstruct
    public JsonNode loadConfiguration() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = resourceFile.getInputStream();
        jsonArray = objectMapper.readTree(inputStream);
        return jsonArray;
    }
}