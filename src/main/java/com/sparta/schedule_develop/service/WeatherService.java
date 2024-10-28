package com.sparta.schedule_develop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Autowired
    public WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getTodayWeather() {
//        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=37.514575&lon=127.0495556&appid=22603c74540255fab49a9851641215bd";
        String url = "https://f-api.github.io/f-api/weather.json";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Check if the "list" array exists and has at least one entry
            if (rootNode.has("list") && rootNode.path("list").isArray() && rootNode.path("list").size() > 0) {
                JsonNode todayWeatherNode = rootNode.path("list").get(0);

                // Check for weather description and temperature fields
                if (todayWeatherNode.has("weather") && todayWeatherNode.path("weather").isArray() &&
                        todayWeatherNode.path("weather").size() > 0 &&
                        todayWeatherNode.has("main")) {

                    String description = todayWeatherNode.path("weather").get(0).path("description").asText();
                    String temperature = todayWeatherNode.path("main").path("temp").asText();

                    return String.format("Today's weather: %s, Temperature: %s°C", description, temperature);
                }
            }
            return "Weather information is unavailable or in an unexpected format";

        } catch (JsonProcessingException | RestClientException e) {
            e.printStackTrace();
            return "Unable to retrieve or parse weather data";
        }
    }
}
