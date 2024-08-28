package com.example.tires.model;

import lombok.Data;
import java.util.List;

@Data
public class ListResponse {
    private List<Field> fields;
    private List<Wrapper> wrappers;
}
