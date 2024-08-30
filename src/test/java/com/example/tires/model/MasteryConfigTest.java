package com.example.tires.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MasteryConfigTest {

    @Test
    public void masteryConfig_getMediaType_returnMediaTypes() {
        MasteryConfig masteryConfigXml = MasteryConfig.builder().mediaType("xml").build();
        MediaType mediaType1 = masteryConfigXml.getMediaType();
        assertThat(mediaType1).isNotNull();
        assertThat(mediaType1).isInstanceOf(MediaType.class);
        assertEquals(mediaType1, MediaType.TEXT_XML);

        MasteryConfig masteryConfigJson = MasteryConfig.builder().mediaType("json").build();
        MediaType mediaType2 = masteryConfigJson.getMediaType();
        assertThat(mediaType2).isNotNull();
        assertThat(mediaType2).isInstanceOf(MediaType.class);
        assertEquals(mediaType2, MediaType.APPLICATION_JSON);

        MasteryConfig masteryConfigRandom = MasteryConfig.builder().mediaType("random").build();
        assertThatThrownBy(masteryConfigRandom::getMediaType)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MediaType not defined");
    }

    @Test
    public void masteryConfig_getContentType_returnMediaTypes() {
        MasteryConfig masteryConfigXml = MasteryConfig.builder().mediaType("xml").build();
        String contentType1 = masteryConfigXml.getContentType();
        assertThat(contentType1).isNotNull();
        assertThat(contentType1).isInstanceOf(String.class);
        assertEquals(contentType1, "text/xml");

        MasteryConfig masteryConfigJson = MasteryConfig.builder().mediaType("json").build();
        String contentType2 = masteryConfigJson.getContentType();
        assertThat(contentType2).isNotNull();
        assertThat(contentType2).isInstanceOf(String.class);
        assertEquals(contentType2, "application/json");

        MasteryConfig masteryConfigRandom = MasteryConfig.builder().mediaType("random").build();
        masteryConfigRandom.setMediaType("random");
        assertThatThrownBy(masteryConfigRandom::getContentType)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MediaType not defined");
    }
}
