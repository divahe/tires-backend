package com.example.tires.model;

import lombok.*;
import org.springframework.http.MediaType;

import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasteryConfig {
    private Mastery mastery;
    private String baseUrl;
    private String listUrl;
    private BookingUrl bookingUrl;
    private String mediaType;
    private BookingRequestApi bookingRequest;
    private BookingResponseApi bookingResponse;
    private ListRequest listRequest;
    private ListResponse listResponse;
    private ErrorResponse errorResponse;

    public MediaType getMediaType() {
        if (Objects.equals(mediaType, "xml")) {
            return MediaType.TEXT_XML;
        }
        else if (Objects.equals(mediaType, "json")) {
            return MediaType.APPLICATION_JSON;
        }
        throw new IllegalArgumentException("MediaType not defined");
    }

    public String getContentType() {
        if (Objects.equals(mediaType, "xml")) {
            return "text/xml";
        }
        else if (Objects.equals(mediaType, "json")) {
            return "application/json";
        }
        throw new IllegalArgumentException("MediaType not defined");
    }
}
