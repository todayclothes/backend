package com.seungah.todayclothes.domain.weather.service;

import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.weather.dto.response.DailyWeatherResponse;
import com.seungah.todayclothes.domain.weather.dto.response.HourlyWeatherResponse;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.entity.HourlyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.domain.weather.repository.HourlyWeatherRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.TimeOfDay;
import com.seungah.todayclothes.global.type.WeatherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.seungah.todayclothes.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherService {
    private final DailyWeatherRepository dailyWeatherRepository;
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;

    @Value("${weather.key}")
    private String key;
    @Value("${weather.url}")
    private String url;
    @Value("${weather.img}")
    private String imgUrl;

    @Scheduled(cron = "0 0 3,15 * * *")
    @Transactional
    public void saveWeather(){
        regionRepository.findAll().forEach(region -> {
            String jsonString = oneCallApi(region);
            try {
                saveHourlyWeather(jsonString, region);
                saveDailyWeather(jsonString, region);
            } catch (ParseException e){
                throw new CustomException(FAILED_CALL_OPENWEATHERMAP_API);
            }
        });
    }
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void deleteWeather(){
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        hourlyWeatherRepository.deleteHourlyWeatherBefore(yesterday);
        dailyWeatherRepository.deleteDailyWeatherBefore(yesterday);
    }
    public String oneCallApi(Region region){
        String apiUrl = String.format("%s?lat=%s&lon=%s&units=metric&exclude=current,minutely,alerts&appid=%s",
                url, region.getLatitude(), region.getLongitude(), key);
        return new RestTemplate().getForObject(apiUrl, String.class);
    }

    public void saveHourlyWeather(String jsonString, Region region) throws ParseException{
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONArray weatherArray = (JSONArray) jsonObject.get("hourly");

        for (Object object : weatherArray) {
            JSONObject weather = (JSONObject) object;

            LocalDateTime date = unixToDate(Long.valueOf(String.valueOf(weather.get("dt"))));
            Double temp = Double.parseDouble(String.valueOf(weather.get("temp")));
            String iconId = (String) ((JSONObject) ((JSONArray) weather.get("weather")).get(0)).get("icon");
            String description = WeatherType.from(iconId).getDescription();
            String icon = imgUrl + iconId + "@2x.png";
            saveHourlyWeatherJson(region, date, temp, description, icon);
        }
    }
    public void saveDailyWeather(String jsonString,Region region) throws ParseException{
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        JSONArray weatherArray = (JSONArray) jsonObject.get("daily");

        for (Object object : weatherArray) {
            JSONObject weather = (JSONObject) object;

            LocalDateTime localDateTime = unixToDate(Long.valueOf(String.valueOf(weather.get("dt"))));
            JSONObject tempObject = (JSONObject) weather.get("temp");

            Double morningTemp = Double.parseDouble(String.valueOf(tempObject.get("morn")));
            Double dayTemp = Double.parseDouble(String.valueOf(tempObject.get("day")));
            Double nightTemp = Double.parseDouble(String.valueOf(tempObject.get("night")));
            Double lowTemp = Double.parseDouble(String.valueOf(tempObject.get("min")));
            Double highTemp = Double.parseDouble(String.valueOf(tempObject.get("max")));

            Map<TimeOfDay, Double> avgTemps = new HashMap<>();
            avgTemps.put(TimeOfDay.MORNING, morningTemp);
            avgTemps.put(TimeOfDay.AFTERNOON, dayTemp);
            avgTemps.put(TimeOfDay.NIGHT, nightTemp);

            Double humidity = Double.parseDouble(String.valueOf(weather.get("humidity")));
            Double windSpeed = Double.parseDouble(String.valueOf(weather.get("wind_speed")));

            Double rain = 0.;
            if (weather.get("rain") != null) {
                rain = Double.parseDouble(String.valueOf(weather.get("rain")));
            }

            saveDailyWeatherJson(region, localDateTime, lowTemp, highTemp, avgTemps, humidity, rain, windSpeed);
        }
    }
    private void saveHourlyWeatherJson(Region region, LocalDateTime date, Double temp, String description, String icon) {
        HourlyWeather hourlyWeather = hourlyWeatherRepository.findByDateAndRegion(date, region);
        if (hourlyWeather == null) {
            hourlyWeather = HourlyWeather.builder().date(date).temp(temp)
                    .description(description).icon(icon).region(region).build();
        } else {
            hourlyWeather.from(temp, description, icon);
        }
        hourlyWeatherRepository.save(hourlyWeather);
    }
    private void saveDailyWeatherJson(Region region, LocalDateTime date, Double lowTemp, Double highTemp,
                                      Map<TimeOfDay, Double> avgTemps, Double humidity, Double rain, Double windSpeed) {

        if (dailyWeatherRepository.findByDateAndRegion(date, region).isEmpty()) {
            dailyWeatherRepository.save(DailyWeather.builder().region(region).date(date).lowTemp(lowTemp).
                    highTemp(highTemp).avgTemps(avgTemps).humidity(humidity).rain(rain).windSpeed(windSpeed).build());
        }
    }
    @Transactional
    public ResponseEntity<HourlyWeatherResponse> getHourlyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withMinute(0).withSecond(0).withNano(0);
        Region region = userId == null ? regionRepository.findByName("서울특별시")
                : regionRepository.findByName(memberRepository.findById(userId).get().getRegion());
        HourlyWeather hourlyWeather = hourlyWeatherRepository.findByDateAndRegion(localDateTime, region);
        if (hourlyWeather == null){
            throw new CustomException(NOT_FOUND_HOURLY_WEATHER);
        }

        return ResponseEntity.ok(new HourlyWeatherResponse().of(hourlyWeather));
    }
    @Transactional
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0);
        Region region = userId == null ? regionRepository.findByName("서울특별시")
                : regionRepository.findByName(memberRepository.findById(userId).get().getRegion());

        Optional<DailyWeather> dailyWeather = dailyWeatherRepository.findByDateAndRegion(localDateTime, region);
        if (dailyWeather.isEmpty()) {
            throw new CustomException(NOT_FOUND_DAILY_WEATHER);
        }
        Double highTemp = dailyWeather.get().getHighTemp();
        Double lowTemp = dailyWeather.get().getLowTemp();
        return ResponseEntity.ok(new DailyWeatherResponse().of(highTemp, lowTemp));
    }

    public LocalDateTime unixToDate(Long unixTime){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), TimeZone.getDefault().toZoneId());
    }
}
