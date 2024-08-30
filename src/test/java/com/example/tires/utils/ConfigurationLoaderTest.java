package com.example.tires.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

@SpringBootTest
public class ConfigurationLoaderTest {

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Test
    public void configurationLoader_LoadConfiguration_loadApiConfiguration() throws IOException {
        JsonNode jsonArray = configurationLoader.loadConfiguration();
        assertThat(jsonArray).isNotNull();
        assertThat(jsonArray).isInstanceOf(JsonNode.class);
        assertTrue(jsonArray.isArray());
        assertEquals(2, jsonArray.size());
    }
}
