package com.example.tires.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private UUID uuid;
    private String identifier;
    private Integer id;
    private Boolean available = true;
    private Mastery mastery;
    private String time;
}
