package com.example.tires.helpers;

import com.example.tires.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helpers {
    public static Map<String, String> createUserInputs() {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("contactInformation", "Diana");
        return inputs;
    }

    public static MasteryConfig createMasteryConfig() {
        Mastery mastery = Mastery.builder().name("Mastery2").address("1A Gunton Rd, London").carTypes(List.of("car")).build();
        UrlString urlString = UrlString.builder().mapper("static").value("/booking").order(0).build();
        List<UrlString> urlStringList = new ArrayList<>();
        urlStringList.add(urlString);
        BookingUrl bookingUrl = BookingUrl.builder().urlStrings(urlStringList).build();
        BookingRequestApi bookingRequest = BookingRequestApi.builder()
                .requestType("POST").fields(new ArrayList<>()).wrappers(new ArrayList<>()).build();
        Field field1 = Field.builder().name("id").mapper("identifier").format("integer").build();
        Field field2 = Field.builder().name("time").mapper("time").format("string").build();
        Field field3 = Field.builder().name("available").mapper("available").format("boolean").build();

        BookingResponseApi bookingResponse = BookingResponseApi.builder().fields(List.of(field1, field2, field3))
                .wrappers(new ArrayList<>()).build();
        ListRequest listRequest = ListRequest.builder().dateFormat("YYY-MM-DD").params(new ArrayList<>()).build();
        ListResponse listResponse = ListResponse.builder().fields(new ArrayList<>()).wrappers(new ArrayList<>()).build();
        Field field4 = Field.builder().name("error").mapper("errorMessage").format("string").build();
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessageTextField("Error:").fields(List.of(field4))
                .wrappers(new ArrayList<>()).build();
        return MasteryConfig
                .builder().mastery(mastery).baseUrl("http://localhost:9003/api/v1/tire-change-times")
                .listUrl("/available").bookingUrl(bookingUrl).mediaType("xml").bookingRequest(bookingRequest)
                .bookingResponse(bookingResponse).listRequest(listRequest).listResponse(listResponse)
                .errorResponse(errorResponse).build();
    }
}
