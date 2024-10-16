package com.sparta.schedule_develop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {


    private final RestTemplate restTemplate;


    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTodayWeather() {
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=37.514575&lon=127.0495556&appid=22603c74540255fab49a9851641215bd";
//        String url = "https://f-api.github.io/f-api/weather.json";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            return "날씨를 가져올 수 없습니다";
        }
    }
}
