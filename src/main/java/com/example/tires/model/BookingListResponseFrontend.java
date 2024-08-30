package com.example.tires.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingListResponseFrontend {
    private List<Booking> bookings;
    private Map<String, String> errors;
}
