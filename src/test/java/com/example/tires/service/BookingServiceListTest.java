package com.example.tires.service;

import com.example.tires.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
public class BookingServiceListTest {
    private ClientAndServer mockServer1;

    private ClientAndServer mockServer2;

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    public void startServer() {
        mockServer1 = startClientAndServer(1080);
        mockServer2 = startClientAndServer(1081);
    }

    @AfterTestClass
    public void stopServer() {
        mockServer1.stop();
        mockServer2.stop();
    }

    @Test
    public void bookingService_findBookings_returnBookingList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String start = now.format(formatter);
        String end = now.plusDays(35).format(formatter);

        mockServer1.when(request()
                        .withMethod("GET")
                        .withPath("/api/v1/tire-change-times/available")
                        .withQueryStringParameter("from", start)
                        .withQueryStringParameter("until", end))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "text/xml;"))
                        .withBody("<tireChangeTimesResponse><availableTime><uuid>5f5ba9b0-f44c-4853-9bcb-f7d77045d107</uuid><time>2024-08-28T06:00:00Z</time></availableTime><availableTime><uuid>294cfd15-ad46-48f1-a435-4016a48a9aca</uuid><time>2024-08-28T08:00:00Z</time></availableTime><availableTime><uuid>d076dc14-c4a9-489c-b813-8bafbfc24a51</uuid><time>2024-08-28T09:00:00Z</time></availableTime><availableTime><uuid>6db12e12-b0c8-4e32-9a59-1ae05c1ac9e0</uuid><time>2024-08-28T10:00:00Z</time></availableTime><availableTime><uuid>2610efe3-3602-4ebc-a141-bacaeec1e3bb</uuid><time>2024-08-28T11:00:00Z</time></availableTime><availableTime><uuid>5f9487e7-378f-494a-91b9-ab7aa13f80ef</uuid><time>2024-08-28T13:00:00Z</time></availableTime></tireChangeTimesResponse>")
                        .withDelay(TimeUnit.SECONDS, 1));

        mockServer2.when(request()
                        .withMethod("GET")
                        .withPath("/api/v2/tire-change-times")
                        .withQueryStringParameter("amount", "200")
                        .withQueryStringParameter("from", start))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "application/json;"))
                        .withBody("[{\"id\":46,\"time\":\"2024-08-28T05:00:00Z\",\"available\":true},{\"id\":47,\"time\":\"2024-08-28T06:00:00Z\",\"available\":true},{\"id\":48,\"time\":\"2024-08-28T07:00:00Z\",\"available\":false}]")
                        .withDelay(TimeUnit.SECONDS, 1));
        BookingListResponseFrontend response = bookingService.findBookings();

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(BookingListResponseFrontend.class);
        List<Booking> bookings = response.getBookings();
        assertEquals( 8, bookings.size());
        assertNull(response.getErrors());
        Booking booking1 = bookings.get(5);
        assertEquals("Mastery1", booking1.getMastery().getName());
        assertEquals(1, booking1.getMastery().getCarTypes().size());
        assertEquals("5f9487e7-378f-494a-91b9-ab7aa13f80ef", booking1.getIdentifier());
        assertEquals("2024-08-28T13:00:00Z", booking1.getTime());
        assertEquals(true, booking1.getAvailable());
        Booking booking2 = bookings.get(7);
        assertEquals("Mastery2", booking2.getMastery().getName());
        assertEquals(2, booking2.getMastery().getCarTypes().size());
        assertEquals(47, booking2.getId());
        assertEquals("2024-08-28T06:00:00Z", booking2.getTime());
        assertEquals(true, booking2.getAvailable());
        assertEquals(8, bookings.stream().filter(Booking::getAvailable).toList().size());
    }
}
