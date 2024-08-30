package com.example.tires.model.mapper;

import com.example.tires.model.Booking;
import com.example.tires.model.BookingRequestFrontend;
import com.example.tires.model.MasteryConfig;
import com.example.tires.service.MasteryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingMapperTest {

    @Autowired
    private MasteryService masteryService;

    @Autowired
    private BookingMapper bookingMapper;

    @Test
    public void bookingMapper_mapToBookingObjects_returnsCorrectObjects() {
        String data1 = "<tireChangeTimesResponse><availableTime><uuid>294cfd15-ad46-48f1-a435-4016a48a9aca</uuid><time>2024-08-28T08:00:00Z</time></availableTime><availableTime><uuid>d076dc14-c4a9-489c-b813-8bafbfc24a51</uuid><time>2024-08-28T09:00:00Z</time></availableTime><availableTime><uuid>6db12e12-b0c8-4e32-9a59-1ae05c1ac9e0</uuid><time>2024-08-28T10:00:00Z</time></availableTime><availableTime><uuid>2610efe3-3602-4ebc-a141-bacaeec1e3bb</uuid><time>2024-08-28T11:00:00Z</time></availableTime><availableTime><uuid>5f9487e7-378f-494a-91b9-ab7aa13f80ef</uuid><time>2024-08-28T13:00:00Z</time></availableTime></tireChangeTimesResponse>";
        MasteryConfig config1 = masteryService.getMasteryConfig("Mastery1");
        List<Booking> bookings1 = bookingMapper.mapToBookingObjects(config1, data1);
        assertThat(bookings1).isNotNull();
        assertThat(bookings1).isInstanceOf(ArrayList.class);
        assertEquals( 5, bookings1.size());
        assertEquals("Mastery1", bookings1.get(1).getMastery().getName());
        assertEquals(1, bookings1.get(1).getMastery().getCarTypes().size());
        assertEquals("d076dc14-c4a9-489c-b813-8bafbfc24a51", bookings1.get(1).getIdentifier());
        assertEquals("2024-08-28T09:00:00Z", bookings1.get(1).getTime());
        assertEquals(true, bookings1.get(1).getAvailable());

        String data2 = "[{\"id\":46,\"time\":\"2024-08-28T05:00:00Z\",\"available\":false},{\"id\":47,\"time\":\"2024-08-28T06:00:00Z\",\"available\":true},{\"id\":48,\"time\":\"2024-08-28T07:00:00Z\",\"available\":false}]";
        MasteryConfig config2 = masteryService.getMasteryConfig("Mastery2");
        List<Booking> bookings2 = bookingMapper.mapToBookingObjects(config2, data2);
        assertThat(bookings2).isNotNull();
        assertThat(bookings2).isInstanceOf(ArrayList.class);
        assertEquals( 3, bookings2.size());
        assertEquals("Mastery2", bookings2.get(1).getMastery().getName());
        assertEquals(2, bookings2.get(1).getMastery().getCarTypes().size());
        assertEquals(47, bookings2.get(1).getId());
        assertEquals("2024-08-28T06:00:00Z", bookings2.get(1).getTime());
        assertEquals(true, bookings2.get(1).getAvailable());
    }

    @Test
    public void bookingMapper_createBookingRequestBody_returnsCorrectXmlRquest() {
        MasteryConfig config1 = masteryService.getMasteryConfig("Mastery1");
        BookingRequestFrontend bookingRequest1 = new BookingRequestFrontend();
        Map<String, String> inputs1 = new HashMap<>();
        inputs1.put("contactInformation", "Diana");
        bookingRequest1.setInputs(inputs1);
        bookingRequest1.setIdentifier("294cfd15-ad46-48f1-a435-4016a48a9aca");
        bookingRequest1.setName("Mastery1");
        String result1 = bookingMapper.createBookingRequestBody(config1, bookingRequest1);
        assertThat(result1).isNotNull();
        assertThat(result1).isInstanceOf(String.class);
        assertEquals("<?xml version='1.0' encoding='UTF-8'?><london.tireChangeBookingRequest><contactInformation>Diana</contactInformation></london.tireChangeBookingRequest>", result1);
    }
    @Test
    public void bookingMapper_createBookingRequestBody_returnsCorrectJsonRequest() {
        MasteryConfig config2 = masteryService.getMasteryConfig("Mastery2");
        BookingRequestFrontend bookingRequest2 = new BookingRequestFrontend();
        Map<String, String> inputs2 = new HashMap<>();
        inputs2.put("contactInformation", "Diana");
        bookingRequest2.setInputs(inputs2);
        bookingRequest2.setIdentifier("46");
        bookingRequest2.setName("Mastery2");
        String result2 = bookingMapper.createBookingRequestBody(config2, bookingRequest2);
        assertThat(result2).isNotNull();
        assertThat(result2).isInstanceOf(String.class);
        assertEquals("{\"testWrapper\":{\"contactInformation\":\"Diana\"}}", result2);
    }
}
