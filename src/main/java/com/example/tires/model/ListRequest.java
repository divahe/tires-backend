package com.example.tires.model;

import lombok.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListRequest{
    private String dateFormat;
    private List<Parameter> params;

    public Map<String, String> getParams() {
        Map<String, String> parameters = new HashMap<>();
        LocalDate now = LocalDate.now();
        List<String> calculatedDateParams = Arrays.asList("start", "end");
        for (Parameter param : params) {
            if (calculatedDateParams.contains(param.getMapper())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                parameters.put(param.getName(), now.plusDays(Integer.parseInt(param.getValue())).format(formatter));
            }
            else {
                parameters.put(param.getName(), param.getValue());
            }
        }
        return parameters;
    }
}
