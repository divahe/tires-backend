package com.example.tires.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingUrlTest {

    @Mock
    private MasteryConfig masteryConfig;

    @Test
    public void bookingUrl_getUrlWithVariables_returnsCorrectUrl() {
        List<UrlString> urlStrings = new ArrayList<>();
        urlStrings.add(UrlString.builder().order(1).value("").mapper("identifier").build());
        urlStrings.add(UrlString.builder().order(2).value("/booking").mapper("static").build());
        urlStrings.add(UrlString.builder().order(0).value("/").mapper("static").build());
        BookingUrl url = BookingUrl.builder().urlStrings(urlStrings).build();
        when(masteryConfig.getBookingUrl()).thenReturn(url);
        BookingUrl url1 = masteryConfig.getBookingUrl();
        String urlWithVariables1 = url1.getUrlWithVariables("d076dc14-c4a9-489c-b813-8bafbfc24a51");
        assertThat(urlWithVariables1).isNotNull();
        assertThat(urlWithVariables1).isInstanceOf(String.class);
        assertEquals( "/d076dc14-c4a9-489c-b813-8bafbfc24a51/booking", urlWithVariables1);
    }
}
