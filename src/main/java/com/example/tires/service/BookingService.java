package com.example.tires.service;

import com.example.tires.model.*;
import com.example.tires.model.mapper.BookingMapper;
import com.example.tires.utils.WebClientUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.tires.model.mapper.JsonMapper;
import org.apache.commons.text.StringEscapeUtils;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
public class BookingService {
    private final MasteryService masteryService;
    private final BookingMapper bookingMapper;
    private final WebClientUtil webClientUtil;
    private final JsonMapper jsonMapper;

    public BookingListResponseFrontend findBookings() {
        List<MasteryConfig> masteries = masteryService.getAllMasteryConfigs();
        List<Booking> bookings = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();
        for (MasteryConfig mastery : masteries) {
            try {
                String rawString = webClientUtil.getRequest(mastery);
                List<Booking> bookingObjectList = bookingMapper.mapToBookingObjects(mastery, rawString);
                bookings.addAll(bookingObjectList);
            } catch (Exception e) {
                errors.put(mastery.getMastery().getName(), e.getMessage());
            }
        }
        BookingListResponseFrontend response = new BookingListResponseFrontend();
        if (!errors.isEmpty()) {
            response.setErrors(errors);
        }
        response.setBookings(filterAvailableBookings(bookings));
        return response;
    }

    private List<Booking> filterAvailableBookings(List<Booking> bookings) {
        return bookings.stream().filter(Booking::getAvailable).collect(Collectors.toList());
    }

    public ResponseEntity<BookingResponseFrontend> makeBooking(BookingRequestFrontend bookingRequest) {
        BookingResponseFrontend frontendResponse = new BookingResponseFrontend();
        frontendResponse.setInfo("Internal Server Error");
        try {
            MasteryConfig config = masteryService.getMasteryConfig(bookingRequest.getName());
            frontendResponse.setMastery(config.getMastery());
            String requestBody = bookingMapper.createBookingRequestBody(config, bookingRequest);
            HttpResponse<String> response = webClientUtil.bookingRequest(config, bookingRequest, requestBody);
            JsonElement responseBody = jsonMapper.mapToJson(config.getMediaType(), response.body());
            if (response.statusCode() == 200) {
                return getBookingResponseEntity(config, responseBody, frontendResponse);
            } else {
                String responseText = getErrorResponse(config, responseBody);
                frontendResponse.setInfo(responseText);
                return new ResponseEntity<>(frontendResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(frontendResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<BookingResponseFrontend> getBookingResponseEntity(
            MasteryConfig config, JsonElement responseBody, BookingResponseFrontend response) {
        JsonElement data = jsonMapper.removeJsonWrappers(config.getBookingResponse().getWrappers(), responseBody);
        response.setInfo("Booking confirmed");
        if (data.isJsonObject()) {
            JsonObject jsonObject = data.getAsJsonObject();
            List<Field> fields = config.getBookingResponse().getFields();
            for (Field field : fields) {
                if (Objects.equals(field.getMapper(), "identifier")) {
                    response.setId(jsonObject.get(field.getName()).getAsString());
                } else if (Objects.equals(field.getMapper(), "time")){
                    response.setTime(jsonObject.get(field.getName()).getAsString());
                }
            }
        } else {
            response.setInfo("Server configuration error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getErrorResponse(MasteryConfig config, JsonElement responseBody) {
        JsonElement data = jsonMapper.removeJsonWrappers(config.getErrorResponse().getWrappers(), responseBody);
        String responseText = "Server configuration error";
        if (data.isJsonObject()) {
            JsonObject jsonObject = data.getAsJsonObject();
            List<Field> fields = config.getErrorResponse().getFields();
            Field field = fields.stream().filter(f -> "errorMessage".equals(f.getMapper())).findFirst().orElse(null);
            if (field != null) {
                JsonElement message = jsonObject.get(field.getName());
                responseText = StringEscapeUtils.unescapeHtml4(message.getAsString());

                String regex = String.format("%s(.*)", config.getErrorResponse().getErrorMessageTextField());
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(responseText);
                if (matcher.find()) {
                    responseText = matcher.group(1).trim();
                }
            }
        }
        return responseText;
    }
}
