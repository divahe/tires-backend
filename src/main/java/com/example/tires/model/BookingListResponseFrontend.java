package com.example.tires.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class BookingListResponseFrontend {
    private List<Booking> bookings;
    private Map<String, String> errors;
}
