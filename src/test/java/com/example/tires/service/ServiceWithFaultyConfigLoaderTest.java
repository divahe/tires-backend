package com.example.tires.service;

import com.example.tires.utils.ConfigurationLoader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ServiceWithFaultyConfigLoaderTest {

    @Mock
    private ConfigurationLoader configurationLoader;

    @InjectMocks
    private MasteryService masteryService;

    @Test
    public void masteryService_getAllMasteryConfigs_trowsError() {
        Throwable exception = assertThrows(RuntimeException.class, () -> masteryService.getAllMasteryConfigs());
        assertEquals("Error on loading configuration", exception.getMessage());
    }
}
