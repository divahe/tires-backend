package com.example.tires.model;

import lombok.Data;

import java.util.*;

@Data
public class BookingUrl {
    private List<UrlString> urlStrings;

    public String getUrlWithVariables(String identifier) {
        urlStrings.sort(Comparator.comparing(UrlString::getOrder));
        StringBuilder sb = new StringBuilder();
        for (UrlString urlString : urlStrings) {
            if (Objects.equals(urlString.getMapper(), "static")) {
               sb.append(urlString.getValue());
            } else {
                sb.append(identifier);
            }
        }
        return sb.toString();
    }
}
