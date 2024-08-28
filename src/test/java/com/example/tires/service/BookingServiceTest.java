package com.example.tires.service;


import com.example.tires.model.BookingListResponseFrontend;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookingServiceTest {

    @InjectMocks
    BookingService bookingService;


    @Test
    public void bookingService_findBookings_returnBookingList() {
        BookingListResponseFrontend response = bookingService.findBookings();
    }

}
