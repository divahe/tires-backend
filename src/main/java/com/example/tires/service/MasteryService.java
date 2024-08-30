package com.example.tires.service;

import com.example.tires.model.MasteryConfig;
import com.example.tires.utils.ConfigurationLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Data
@Service

public class MasteryService {
    private final ConfigurationLoader configLoader;
    private final ObjectMapper objectMapper;

    public List<MasteryConfig> getAllMasteryConfigs() {
        try {
            JsonNode jsonArray = configLoader.loadConfiguration();
            List<MasteryConfig> masteries = new ArrayList<>();
            if (jsonArray == null) {
                throw new RuntimeException("Error on loading configuration");
            }
            if (jsonArray.isArray()) {
                Iterator<JsonNode> elements = jsonArray.elements();
                while (elements.hasNext()) {
                    JsonNode element = elements.next();
                    MasteryConfig mastery = objectMapper.convertValue(element, MasteryConfig.class);
                    masteries.add(mastery);
                }
            }
            return masteries;
        } catch (IOException e) {
            throw new RuntimeException(" Error on loading configuration");
        }
    }

    public MasteryConfig getMasteryConfig(String name) {
        List<MasteryConfig> masteryConfigs = getAllMasteryConfigs();
        return masteryConfigs.stream().filter(config -> Objects.equals(config.getMastery().getName(), name)).findFirst().orElse(null);
    }
}

