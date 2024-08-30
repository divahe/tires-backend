package com.example.tires.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ListRequestTest {

    @Test
    public void listRequest_getParams_returnsParameters() {
        List<Parameter> params = new ArrayList<>();
        Parameter param1 = Parameter.builder().name("from")
                .mapper("start").value("0").build();
        Parameter param2 = Parameter.builder().name("until")
                .mapper("end").value("35").build();
        params.add(param1);
        params.add(param2);
        ListRequest listRequest = ListRequest.builder().params(params).dateFormat("YYYY-MM-dd").build();
        LocalDate now1 = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String dateNow = now1.format(formatter);
        String limit = now1.plusDays(35).format(formatter);
        assertThat(listRequest.getParams()).isNotNull();
        assertThat(listRequest.getParams()).isInstanceOf(Map.class);
        assertTrue(listRequest.getParams().containsKey("from"));
        assertTrue(listRequest.getParams().containsKey("until"));
        assertEquals(listRequest.getParams().get("from"), dateNow);
        assertEquals(listRequest.getParams().get("until"), limit);
    }

}
