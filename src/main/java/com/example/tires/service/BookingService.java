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
        List<Booking> bookings = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();
        for (MasteryConfig masteryConfig : masteryService.getAllMasteryConfigs()) {
            getBookingsFromMastery(masteryConfig, bookings, errors);
        }
        return getBookingListResponseFrontend(errors, bookings);
    }

    private BookingListResponseFrontend getBookingListResponseFrontend(Map<String, String> errors, List<Booking> bookings) {
        BookingListResponseFrontend response = new BookingListResponseFrontend();
        if (!errors.isEmpty()) {
            response.setErrors(errors);
        }
        response.setBookings(filterAvailableBookings(bookings));
        return response;
    }

    private void getBookingsFromMastery(MasteryConfig masteryConfig, List<Booking> bookings, Map<String, String> errors) {
        try {
            String rawString = webClientUtil.getRequest(masteryConfig);
            List<Booking> bookingObjectList = bookingMapper.mapToBookingObjects(masteryConfig, rawString);
            bookings.addAll(bookingObjectList);
        } catch (Exception e) {
            errors.put(masteryConfig.getMastery().getName(), e.getMessage());
        }
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
                JsonElement data = jsonMapper.removeJsonWrappers(config.getBookingResponse().getWrappers(), responseBody);
                if (data.isJsonObject()) {
                    frontendResponse.setInfo("Booking confirmed");
                    JsonObject jsonObject = data.getAsJsonObject();
                    List<Field> fields = config.getBookingResponse().getFields();
                    for (Field field : fields) {
                        if (Objects.equals(field.getMapper(), "identifier")) {
                            frontendResponse.setId(jsonObject.get(field.getName()).getAsString());
                        } else if (Objects.equals(field.getMapper(), "time")){
                            frontendResponse.setTime(jsonObject.get(field.getName()).getAsString());
                        }
                    }
                    return new ResponseEntity<>(frontendResponse, HttpStatus.OK);
                } else {
                    frontendResponse.setInfo("Server configuration error");
                    return new ResponseEntity<>(frontendResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                String responseText = getErrorResponse(config.getErrorResponse(), responseBody);
                frontendResponse.setInfo(responseText);
                return new ResponseEntity<>(frontendResponse, HttpStatus.valueOf(response.statusCode()));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(frontendResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getErrorResponse(ErrorResponse errorResponseConfig, JsonElement responseBody) {
        JsonElement data = jsonMapper.removeJsonWrappers(errorResponseConfig.getWrappers(), responseBody);
        String responseText = "Server configuration error";
        if (data.isJsonObject()) {
            JsonObject jsonObject = data.getAsJsonObject();
            List<Field> fields = errorResponseConfig.getFields();
            Field field = fields.stream().filter(f -> "errorMessage".equals(f.getMapper())).findFirst().orElse(null);
            if (field != null) {
                JsonElement message = jsonObject.get(field.getName());
                responseText = StringEscapeUtils.unescapeHtml4(message.getAsString());

                String regex = String.format("%s(.*)", errorResponseConfig.getErrorMessageTextField());
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
