package com.example.tires.model;

import lombok.Data;

import java.util.Map;

@Data
public class BookingRequestFrontend {
    private String id;
    private String identifier;
    private String name;
    private Map<String, String> inputs;
}
