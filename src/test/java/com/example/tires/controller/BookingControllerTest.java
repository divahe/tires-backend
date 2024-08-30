package com.example.tires.controller;

import com.example.tires.helpers.Helpers;
import com.example.tires.model.*;
import com.example.tires.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import java.util.List;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    public void bookingController_findBookings_returnsBookingList() throws Exception {
        BookingListResponseFrontend response = createBookingListResponse();
        given(bookingService.findBookings()).willReturn(response);
        mockMvc.perform(get("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.bookings", hasSize(1)))
                .andExpect(jsonPath("$.bookings[0].identifier", is(response.getBookings().get(0).getIdentifier())));
    }

    @Test
    public void bookingController_makeBooking_returnsBookingDetails() throws Exception {
        ResponseEntity<BookingResponseFrontend> bookingResponse = createSuccessfulBookingResponse();
        BookingRequestFrontend bookingRequest = BookingRequestFrontend.builder().inputs(Helpers.createUserInputs()).name("Mastery2").id("49").build();

        given(bookingService.makeBooking(ArgumentMatchers.any())).willReturn(bookingResponse);
        mockMvc.perform(put("/api/v1/booking/book")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(bookingRequest.getId())));
    }

    @Test
    public void bookingController_makeBooking_returnsErrorFromService() throws Exception {
        ResponseEntity<BookingResponseFrontend> bookingResponse = createFailedBookingResponse();
        BookingRequestFrontend bookingRequest = BookingRequestFrontend.builder().inputs(Helpers.createUserInputs()).name("Mastery2").id("49").build();

        given(bookingService.makeBooking(ArgumentMatchers.any())).willReturn(bookingResponse);
        mockMvc.perform(put("/api/v1/booking/book")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mastery.name", is(bookingRequest.getName())));
    }

    @Test
    public void bookingController_makeBooking_returnsErrorWhenServiceReturnsNull() throws Exception {
        BookingRequestFrontend bookingRequest = BookingRequestFrontend.builder().inputs(Helpers.createUserInputs()).name("Mastery2").id("49").build();
        given(bookingService.makeBooking(ArgumentMatchers.any())).willReturn(null);
        mockMvc.perform(post("/api/v1/booking/book")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    private BookingListResponseFrontend createBookingListResponse() {
        Booking booking = Booking.builder().identifier("6db12e12-b0c8-4e32-9a59-1ae05c1ac9e0").build();
        List<Booking> bookings = Collections.singletonList(booking);
        return BookingListResponseFrontend
                .builder()
                .bookings(bookings)
                .build();
    }

    private ResponseEntity<BookingResponseFrontend> createFailedBookingResponse() {
        return new ResponseEntity<>(createBookingResponseContent(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<BookingResponseFrontend> createSuccessfulBookingResponse() {
        return new ResponseEntity<>(createBookingResponseContent(), HttpStatus.OK);
    }

    private BookingResponseFrontend createBookingResponseContent() {
        Mastery mastery = Mastery
                .builder()
                .name("Mastery2")
                .address("Test road 1")
                .carTypes(Arrays.asList("car", "truck"))
                .build();
        return BookingResponseFrontend
                .builder()
                .mastery(mastery)
                .id("49").build();
    }
}
