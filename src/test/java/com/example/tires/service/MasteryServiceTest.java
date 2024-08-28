package com.example.tires.service;
import com.example.tires.model.MasteryConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MasteryServiceTest {
    @Autowired
    private MasteryService masteryService;

    @Test
    void masteryService_getAllMasteryConfigs_returnMasteryConfigs() {
        List<MasteryConfig> masteryConfigs = masteryService.getAllMasteryConfigs();
        assertThat(masteryConfigs).isNotNull();
        assertThat(masteryConfigs).isInstanceOf(ArrayList.class);
    }

    @Test
    void masteryService_getMasteryConfig_ReturnsCorrectMasteryConfig() {
        MasteryConfig masteryConfig = masteryService.getMasteryConfig("Mastery1");
        assertNotNull(masteryConfig);
        assertEquals("Mastery1", masteryConfig.getMastery().getName());
        assertThat(masteryConfig).isInstanceOf(MasteryConfig.class);
    }

    @Test
    void masteryService_getMasteryConfig_ReturnsNullWhenNotFound() {
        MasteryConfig masteryConfig = masteryService.getMasteryConfig("Random");
        assertNull(masteryConfig);
    }
}
