package com.example.tires.utils;
import com.example.tires.helpers.Helpers;
import com.example.tires.model.BookingRequestFrontend;
import com.example.tires.model.MasteryConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebClientUtilTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private BookingRequestFrontend requestFrontend;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private WebClientUtil webClientUtil;

    private Method getBuiltRequestMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        getBuiltRequestMethod = WebClientUtil.class.getDeclaredMethod("getBuiltRequest",
                String.class, String.class, String.class, String.class);
        getBuiltRequestMethod.setAccessible(true);
    }

    @Test
    public void webClientUtil_bookingRequest_returnsHttpRequest() throws IOException, InterruptedException {
        String identifier = "45";
        String body = "{\"dummyKey\":\"value\"}";
        MasteryConfig masteryConfig = Helpers.createMasteryConfig();

        when(requestFrontend.getIdentifier()).thenReturn(identifier);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        HttpResponse<String> response = webClientUtil.bookingRequest(masteryConfig, requestFrontend, body);

        verify(httpClient).send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()));
        assertEquals(httpResponse, response);
    }

    @Test
    public void webClientUtil_getBuiltRequest_returnsPutHttpRequest() throws InvocationTargetException, IllegalAccessException {
        String url = "http://testUrl/api/v1/";
        String contentType = "application/json";
        String body = "{\"dummyKey\":\"value\"}";

        HttpRequest httpRequest = invokeGetBuiltRequest("PUT", url, contentType, body);

        assertHttpRequest(httpRequest, "PUT", url, contentType);
    }

    @Test
    public void webClientUtil_getBuiltRequest_returnsPostHttpRequest() throws InvocationTargetException, IllegalAccessException {
        String url = "http://testUrl/api/v1/";
        String contentType = "application/json";
        String body = "{\"dummyKey\":\"value\"}";

        HttpRequest httpRequest = invokeGetBuiltRequest("POST", url, contentType, body);

        assertHttpRequest(httpRequest, "POST", url, contentType);
    }

    private HttpRequest invokeGetBuiltRequest(String method, String url, String contentType, String body) throws InvocationTargetException, IllegalAccessException {
        Object responseObject = getBuiltRequestMethod.invoke(webClientUtil, method, url, contentType, body);
        assertTrue(responseObject instanceof HttpRequest);
        return (HttpRequest) responseObject;
    }

    private void assertHttpRequest(HttpRequest httpRequest, String expectedMethod, String expectedUrl, String expectedContentType) {
        assertNotNull(httpRequest);
        assertEquals(expectedUrl, httpRequest.uri().toString());
        assertEquals(expectedMethod, httpRequest.method());
        assertEquals(expectedContentType, httpRequest.headers().firstValue("Content-Type").orElse(""));
    }
}
