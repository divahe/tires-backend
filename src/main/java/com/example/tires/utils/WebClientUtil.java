package com.example.tires.utils;

import com.example.tires.model.*;
import com.example.tires.model.mapper.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

@Data
@Component
public class WebClientUtil {
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public String getRequest(MasteryConfig masteryConfig) {
        Map<String, String> params = masteryConfig.getListRequest().getParams();
        WebClient webClient = WebClient.create(masteryConfig.getBaseUrl());
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(masteryConfig.getListUrl());
                    params.forEach(uriBuilder::queryParam);
                    return uriBuilder.build();
                })
                .accept(masteryConfig.getMediaType())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            String body = clientResponse.toString();
                            throw new RestClientException(jsonMapper.mapToJson(masteryConfig.getMediaType(), body).toString());
                        }).bodyToMono(String.class)
                .block();
    }

    public HttpResponse<String> bookingRequest(MasteryConfig masteryConfig, BookingRequestFrontend requestFrontend, String body) throws IOException, InterruptedException {
            String identifier;
            if (requestFrontend.getIdentifier() != null) {
                identifier = requestFrontend.getIdentifier();
            } else {
                identifier = requestFrontend.getId();
            }
            String url = masteryConfig.getBaseUrl() + masteryConfig.getBookingUrl().getUrlWithVariables(identifier);
            HttpRequest request = getBuiltRequest(masteryConfig.getBookingRequest().getRequestType(), url, masteryConfig.getContentType(), body);
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest getBuiltRequest(String requestType, String url, String contentType, String body) {
        if (Objects.equals(requestType, "PUT")) {
            return HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", contentType)
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } else {
            return HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        }
    }
}
