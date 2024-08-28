package com.example.tires.controller;

import com.example.tires.model.BookingListResponseFrontend;
import com.example.tires.model.BookingRequestFrontend;
import com.example.tires.model.BookingResponseFrontend;
import com.example.tires.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/v1/", produces = { "application/json" })
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final BookingService bookingService;


    @GetMapping("booking")
    public BookingListResponseFrontend findBookings() {
        return bookingService.findBookings();
    }


    @PutMapping("booking/book")
    public ResponseEntity<BookingResponseFrontend> makeBooking(@RequestBody BookingRequestFrontend bookingRequest) {
        return bookingService.makeBooking(bookingRequest);
    }
}
