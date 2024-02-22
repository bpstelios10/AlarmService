package org.alarm.infrastructure;

import org.alarm.application.AlarmDTO;
import org.alarm.application.AlarmNotifierClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class MyHttpClient implements AlarmNotifierClient {

    private final HttpClient httpClient;

    public MyHttpClient() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    public void notify(AlarmDTO alarm) {
        HttpRequest requestForAlarm;
        try {
            requestForAlarm = createRequest(alarm);
        } catch (URISyntaxException ex) {
            System.out.println("error creating the request: " + ex.getMessage());
            throw new RuntimeException(ex);
        }

        HttpResponse<String> response;
        try {
            response = httpClient.send(requestForAlarm, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            //some fallback
            System.out.println("HttpClient failed to send the message, with error: " + ex.getMessage());
            throw new RuntimeException(ex);
        }

        try {
            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println(jsonObject);
        } catch (JSONException ex) {
            System.out.println("Error parsing the response body: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private HttpRequest createRequest(AlarmDTO alarm) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(alarm.urlToRingWhenTrigger()))
                .GET()
                .timeout(Duration.ofMinutes(1))
                .build();
    }
}
