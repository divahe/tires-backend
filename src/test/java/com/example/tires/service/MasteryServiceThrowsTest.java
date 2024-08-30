package com.example.tires.service;

import com.example.tires.utils.ConfigurationLoader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class MasteryServiceThrowsTest {

    @Mock
    private ConfigurationLoader configLoader;

    @InjectMocks
    private MasteryService masteryService;

    @Test
    public void masteryService_getAllMasteryConfigs_catchesIOException() throws IOException {
        ConfigurationLoader loader = mock(ConfigurationLoader.class);
        doThrow(new IOException("Unexpected IO Exception")).when(loader).loadConfiguration();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            masteryService.getAllMasteryConfigs();
        });
        assertThat(exception.getMessage()).isEqualTo("Error on loading configuration");
    }
}
