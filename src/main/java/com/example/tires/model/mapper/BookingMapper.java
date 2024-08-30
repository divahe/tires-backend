package com.example.tires.model.mapper;

import com.example.tires.model.*;
import com.example.tires.utils.Helpers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    private final JsonMapper jsonMapper;

    public List<Booking> mapToBookingObjects(MasteryConfig config, String rawData) {
        try {
            JsonElement jsonElement = jsonMapper.mapToJson(config.getMediaType(), rawData);
            List<Booking> results;
            JsonArray jsonArray = new JsonArray();
            if (jsonElement.isJsonArray()) {
                jsonArray = jsonElement.getAsJsonArray();
            } else {
                JsonElement json = jsonMapper.removeJsonWrappers(config.getListResponse().getWrappers(), jsonElement);
                if (json.isJsonArray()) {
                    jsonArray = json.getAsJsonArray();
                } else if (json.isJsonObject()){
                    jsonArray.add(json.getAsJsonObject());
                } else {
                    throw new RuntimeException("Received data in an unknown format");
                }
            }
            results = parseJsonArrayToBookings(config, jsonArray);
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Error on setup of data content");
        }
    }

    private List<Booking> parseJsonArrayToBookings(MasteryConfig mastery, JsonArray jsonArray) {
        List<Booking> results = new ArrayList<>();
        for (JsonElement jsonBooking : jsonArray) {
            results.add(constructBooking(mastery, jsonBooking));
        }
        return results;
    }

    private Booking constructBooking(MasteryConfig mastery, JsonElement jsonElement) {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        Booking booking = new Booking();
        List<Field> fields = mastery.getListResponse().getFields();
        Field idField = Helpers.getFieldByMapper(fields, "identifier");
        if (idField != null && Objects.equals(idField.getFormat(), "integer")) {
            booking.setId(jsonObj.get(idField.getName()).getAsInt());
        } else if (idField != null && !Objects.equals(idField.getFormat(), "integer")) {
            String fieldName = idField.getName();
            JsonElement el = jsonObj.get(fieldName);
            booking.setIdentifier(el.getAsString());
        }
        Field timeField = Helpers.getFieldByMapper(fields, "time");
        if (timeField != null) {
            booking.setTime(jsonObj.get(timeField.getName()).getAsString());
        }
        Field availabilityField = Helpers.getFieldByMapper(fields, "available");
        if (availabilityField != null) {
            booking.setAvailable(jsonObj.get(availabilityField.getName()).getAsBoolean());
        }
        booking.setUuid(UUID.randomUUID());
        booking.setMastery(mastery.getMastery());
        return booking;
    }

    public String createBookingRequestBody(MasteryConfig masteryConfig, BookingRequestFrontend bookingRequest) {
        if (Objects.equals(masteryConfig.getMediaType(), MediaType.APPLICATION_JSON)) {
            return createJsonBody(masteryConfig, bookingRequest);
        } else {
            return createXmlBody(masteryConfig, bookingRequest);
        }
    }

    private String createXmlBody(MasteryConfig masteryConfig, BookingRequestFrontend bookingRequest) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            ObjectMapper mapper = new ObjectMapper();
            List<Field> fields = masteryConfig.getBookingRequest().getFields();
            Map<String, String> requestValues = bookingRequest.getInputs();
            ObjectNode rootNode = mapper.createObjectNode();
            for (Field field : fields) {
                rootNode.put(field.getName(), requestValues.get(field.getName()));
            }
            for (int i = 1; i < masteryConfig.getBookingRequest().getWrappers().size(); i++) {
                rootNode = rootNode.putObject(masteryConfig.getBookingRequest().getWrappers().get(i).getName());
            }
            return xmlMapper.writer().withRootName(masteryConfig.getBookingRequest().getWrappers().get(0).getName()).writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error on data handling");
        }
    }

    private String createJsonBody(MasteryConfig masteryConfig, BookingRequestFrontend bookingRequest) {
        JsonObject jsonObject = new JsonObject();
        List<Field> fields = masteryConfig.getBookingRequest().getFields();
        Map<String, String> requestValues = bookingRequest.getInputs();
        for (Field field : fields) {
            jsonObject.addProperty(field.getName(), requestValues.get(field.getName()));
        }
        List<Wrapper> wrappers = masteryConfig.getBookingRequest().getWrappers();
        JsonObject result = new JsonObject();
        boolean dataObjectAdded = false;
        if (wrappers != null) {
            for (Wrapper wrapper : wrappers) {
                JsonObject requestWrapper = new JsonObject();
                if (!dataObjectAdded) {
                    requestWrapper.add(wrapper.getName(), jsonObject);
                    result = requestWrapper;
                    dataObjectAdded = true;
                } else {
                    requestWrapper.add(wrapper.getName(), result);
                    result = requestWrapper;
                }
            }
            return result.toString();
        } else {
            return jsonObject.toString();
        }
    }
}
