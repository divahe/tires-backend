package com.example.tires.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Booking {
    private UUID uuid;
    private String identifier;
    private Integer id;
    private Boolean available = true;
    private Mastery mastery;
    private String time;
}
