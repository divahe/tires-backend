package com.example.tires.service;

import com.example.tires.utils.ConfigurationLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
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
