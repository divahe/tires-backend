package com.example.tires.model.mapper;

import com.example.tires.model.Wrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonMapper {
    private final Gson gson = new Gson();

    public JsonElement mapToJson(MediaType mediaType, String data) throws JSONException {
        if (mediaType == MediaType.APPLICATION_JSON) {
            return JsonParser.parseString(data);
        } else if (mediaType == MediaType.TEXT_XML) {
            JSONObject jsonObject = XML.toJSONObject(data);
            return gson.fromJson(jsonObject.toString(), JsonElement.class);
        } else {
            throw new RuntimeException("Unknown response format");
        }
    }

    public JsonElement removeJsonWrappers(List<Wrapper> wrappers, JsonElement element) {
        if (wrappers != null) {
            wrappers.sort(Comparator.comparing(Wrapper::getLevel));
            JsonObject result = element.getAsJsonObject();
            for (Wrapper wrapper : wrappers) {
                JsonElement subResult = result.get(wrapper.getName());
                if (subResult != null && subResult.isJsonObject()) {
                    result = subResult.getAsJsonObject();
                } else {
                    return result.get(wrapper.getName());
                }
            }
            return result;
        }
        return element;
    }
}
