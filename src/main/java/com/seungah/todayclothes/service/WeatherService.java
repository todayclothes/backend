package com.seungah.todayclothes.service;

import com.seungah.todayclothes.dto.HourlyWeatherDto;
import com.seungah.todayclothes.entity.HourlyWeather;
import com.seungah.todayclothes.entity.Region;
import com.seungah.todayclothes.repository.DailyWeatherRepository;
import com.seungah.todayclothes.repository.HourlyWeatherRepository;
import com.seungah.todayclothes.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherService {
    private final DailyWeatherRepository dailyWeatherRepository;
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final RegionRepository repository;

    private final String key = "ebef73b5168e228a84f39f3bbe8ea6da";
    private final String url = "https://api.openweathermap.org/data";


    // one call: https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
    // 5days: https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @Transactional
    public void oneCallApiCall(Region region) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url+"/3.0/onecall?");
        stringBuilder.append("lat="+region.getLatitude()+"&lon="+region.getLongitude());
        stringBuilder.append("&units=metric&exclude=current,minutely,alerts&appid="+key);
        String url = stringBuilder.toString();

        String jsonString = restTemplate.getForObject(url, String.class);
        try {
            saveHourlyWeather(jsonString, region);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//        saveDailyWeather(jsonString);

    }
    public void forecastApiCall(Region region){

    }



    public void saveHourlyWeather(String jsonString, Region region) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(jsonString);
        JSONArray hourlyWeatherInfo = (JSONArray) object.get("hourly");
        for (int i=0;i<hourlyWeatherInfo.size();i++) {
            object = (JSONObject) hourlyWeatherInfo.get(i);
            JSONObject iconObject = (JSONObject)object.get("weather");

            HourlyWeatherDto.builder().date((LocalDateTime) object.get("dt"))
                    .temp((Double) object.get("temp"))
                    .humidity((Double) object.get("humidity"))
                    .icon((String) iconObject.get("icon"))
                    .build();


        }

    }

    public void saveDailyWeather(String jsonString) {

    }
    public LocalDateTime unixToDate (Long unixTime){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTime), TimeZone.getDefault().toZoneId());
    }

}
