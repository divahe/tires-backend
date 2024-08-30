package com.example.tires.service;

import com.example.tires.helpers.Helpers;
import com.example.tires.model.*;
import com.example.tires.model.mapper.BookingMapper;
import com.example.tires.model.mapper.JsonMapper;
import com.example.tires.utils.WebClientUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceBookingTest {

    @Mock
    private MasteryService masteryService;

    @Mock
    private WebClientUtil webClientUtil;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private JsonMapper jsonMapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void bookingService_makeBooking_returnsErrorIfIncorrectResponseBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InterruptedException {
        BookingRequestFrontend bookingRequest = BookingRequestFrontend.builder().inputs(Helpers.createUserInputs()).name("Mastery2").id("49").build();
        MasteryConfig config = Helpers.createMasteryConfig();
        String requestBody = "{\"contactInformation\":\"Diana\"}";
        String responseBodyString = "{\"error\":\"tire change time 85a4bc58-11bd-4714-b19d-1cbc011a8efa is unavailable\",\"statusCode\":422}";
        JsonElement jsonElement = JsonParser.parseString(responseBodyString);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(422);
        when(httpResponse.body()).thenReturn(responseBodyString);
        when(masteryService.getMasteryConfig(anyString())).thenReturn(config);
        when(bookingMapper.createBookingRequestBody(config, bookingRequest)).thenReturn(requestBody);
        when(webClientUtil.bookingRequest(config, bookingRequest, requestBody)).thenReturn(httpResponse);
        when(jsonMapper.mapToJson(any(), any())).thenReturn(jsonElement);
        when(jsonMapper.removeJsonWrappers(anyList(), any())).thenReturn(jsonElement);
        Method makeBooking = BookingService.class.getDeclaredMethod("makeBooking", BookingRequestFrontend.class);
        makeBooking.setAccessible(true);

        Object responseObject = makeBooking.invoke(bookingService, bookingRequest);

        assertEquals("<422 UNPROCESSABLE_ENTITY Unprocessable Entity,BookingResponseFrontend(info=tire change time 85a4bc58-11bd-4714-b19d-1cbc011a8efa is unavailable, mastery=Mastery(name=Mastery2, address=1A Gunton Rd, London, carTypes=[car]), time=null, id=null),[]>", responseObject.toString());
    }

    @Test
    public void bookingService_getErrorResponse_returnsCorrectString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String responseBodyString = "{\"error\":\"tire change time 85a4bc58-11bd-4714-b19d-1cbc011a8efa is unavailable\",\"statusCode\":422}";
        JsonElement jsonElement = JsonParser.parseString(responseBodyString);
        when(jsonMapper.removeJsonWrappers(any(), any())).thenReturn(jsonElement);
        Method getErrorResponse = BookingService.class.getDeclaredMethod("getErrorResponse", ErrorResponse.class, JsonElement.class);
        getErrorResponse.setAccessible(true);
        Wrapper wrapper = Wrapper.builder().name("errorResponse").level(0).build();
        Field field = Field.builder().name("error").format("string").mapper("errorMessage").build();
        ErrorResponse responseSettings = ErrorResponse.builder().wrappers(Collections.singletonList(wrapper)).fields(Collections.singletonList(field)).build();
        Object response = getErrorResponse.invoke(bookingService, responseSettings, jsonElement);
        assertEquals("tire change time 85a4bc58-11bd-4714-b19d-1cbc011a8efa is unavailable", response.toString());
    }

    @Test
    public void bookingService_getErrorResponse_returnsFieldValidationError() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String responseBodyString = "{\"error\":\"Key: 'tireChangeBookingRequest.ContactInformation' Error:Field validation for 'ContactInformation' failed on the 'required' tag\",\"statusCode\":400}";
        JsonElement jsonElement = JsonParser.parseString(responseBodyString);
        when(jsonMapper.removeJsonWrappers(any(), any())).thenReturn(jsonElement);
        Method getErrorResponse = BookingService.class.getDeclaredMethod("getErrorResponse", ErrorResponse.class, JsonElement.class);
        getErrorResponse.setAccessible(true);
        Wrapper wrapper = Wrapper.builder().name("errorResponse").level(0).build();
        Field field = Field.builder().name("error").format("string").mapper("errorMessage").build();
        ErrorResponse responseSettings = ErrorResponse.builder().wrappers(Collections.singletonList(wrapper)).fields(Collections.singletonList(field)).errorMessageTextField("Error:").build();
        Object response = getErrorResponse.invoke(bookingService, responseSettings, jsonElement);
        assertEquals("Field validation for 'ContactInformation' failed on the 'required' tag", response.toString());
    }

    @Test
    public void bookingService_makeBooking_returnBookingResponse() throws IOException, InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BookingRequestFrontend bookingRequest = BookingRequestFrontend.builder().inputs(Helpers.createUserInputs()).name("Mastery2").id("49").build();
        MasteryConfig config = Helpers.createMasteryConfig();
        String requestBody = "{\"contactInformation\":\"Diana\"}";
        String responseBodyString = "{\"id\":49,\"time\":\"2024-08-30T06:00:00Z\",\"available\":false}";
        JsonElement jsonElement = JsonParser.parseString(responseBodyString);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(responseBodyString);
        when(masteryService.getMasteryConfig(anyString())).thenReturn(config);
        when(bookingMapper.createBookingRequestBody(config, bookingRequest)).thenReturn(requestBody);
        when(webClientUtil.bookingRequest(config, bookingRequest, requestBody)).thenReturn(httpResponse);
        when(jsonMapper.mapToJson(any(), any())).thenReturn(jsonElement);
        when(jsonMapper.removeJsonWrappers(anyList(), any())).thenReturn(jsonElement);
        Method makeBooking = BookingService.class.getDeclaredMethod("makeBooking", BookingRequestFrontend.class);
        makeBooking.setAccessible(true);

        Object responseObject = makeBooking.invoke(bookingService, bookingRequest);

        assertEquals("<200 OK OK,BookingResponseFrontend(info=Booking confirmed, mastery=Mastery(name=Mastery2, address=1A Gunton Rd, London, carTypes=[car]), time=2024-08-30T06:00:00Z, id=49),[]>", responseObject.toString());
    }
}
