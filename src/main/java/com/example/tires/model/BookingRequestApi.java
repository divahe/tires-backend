package com.example.tires.model;

import lombok.*;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestApi {
    private String requestType;
    private List<Field> fields;
    private List<Wrapper> wrappers;
    private String contactInfo;
}
