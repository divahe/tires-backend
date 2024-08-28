package com.example.tires.utils;

import com.example.tires.model.Field;

import java.util.List;
import java.util.Objects;

public class Helpers {
    public static Field getFieldByMapper(List<Field>fields, String mapper) {
        for (Field field : fields) {
            if (Objects.equals(field.getMapper(), mapper)) {
                return field;
            }
        }
        return null;
    }
}
