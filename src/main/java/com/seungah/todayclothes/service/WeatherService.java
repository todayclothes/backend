package com.seungah.todayclothes.service;

import com.seungah.todayclothes.common.exception.CustomException;
import com.seungah.todayclothes.dto.response.DailyWeatherResponse;
import com.seungah.todayclothes.dto.response.HourlyWeatherResponse;
import com.seungah.todayclothes.entity.DailyWeather;
import com.seungah.todayclothes.entity.HourlyWeather;
import com.seungah.todayclothes.entity.Region;
import com.seungah.todayclothes.repository.DailyWeatherRepository;
import com.seungah.todayclothes.repository.HourlyWeatherRepository;
import com.seungah.todayclothes.repository.MemberRepository;
import com.seungah.todayclothes.repository.RegionRepository;
import com.seungah.todayclothes.type.TimeOfDay;
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
import java.util.List;
import java.util.TimeZone;

import static com.seungah.todayclothes.common.exception.ErrorCode.FAILED_CALL_OPENWEATHERMAP_API;

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

    @Scheduled(cron = "0 0 3,15 * * *")
    @Transactional
    public void saveWeather(){
        log.info("saveWeather_start");
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
        log.info("deleteWeather_start");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        hourlyWeatherRepository.deleteHourlyWeatherBefore(yesterday);
        dailyWeatherRepository.deleteDailyWeatherBefore(yesterday);
    }
    public String oneCallApi(Region region){
        log.info("apiCall_start");
        String apiUrl = String.format("%s?lat=%s&lon=%s&units=metric&exclude=current,minutely,alerts&appid=%s",
                url, region.getLatitude(), region.getLongitude(), key);
        return new RestTemplate().getForObject(apiUrl, String.class);
    }

    public void saveHourlyWeather(String jsonString, Region region) throws ParseException{
        log.info("saveHourlyWeather_start");
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONArray weatherArray = (JSONArray) jsonObject.get("hourly");

        for (Object object : weatherArray) {
            JSONObject weather = (JSONObject) object;

            LocalDateTime date = unixToDate(Long.valueOf(String.valueOf(weather.get("dt"))));
            Double temp = Double.parseDouble(String.valueOf(weather.get("temp")));
            Double humidity = Double.parseDouble(String.valueOf(weather.get("humidity")));
            String icon = (String) ((JSONObject) ((JSONArray) weather.get("weather")).get(0)).get("icon");
            saveHourlyWeatherJson(region, date, temp, humidity, icon);
        }
    }
    public void saveDailyWeather(String jsonString,Region region) throws ParseException{
        log.info("saveDailyWeather_start");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        JSONArray weatherArray = (JSONArray) jsonObject.get("daily");

        for (Object object : weatherArray) {
            JSONObject weather = (JSONObject) object;

            LocalDateTime localDateTime = unixToDate(Long.valueOf(String.valueOf(weather.get("dt"))));
            JSONObject tempObject = (JSONObject) weather.get("temp");

            Double morningTemp = Double.parseDouble(String.valueOf(tempObject.get("morn")));
            Double dayTemp = Double.parseDouble(String.valueOf(tempObject.get("day")));
            Double nightTemp = Double.parseDouble(String.valueOf(tempObject.get("eve")));

            Double humidity = Double.parseDouble(String.valueOf(weather.get("humidity")));
            Double windSpeed = Double.parseDouble(String.valueOf(weather.get("wind_speed")));

            Double rain = 0.;
            if (weather.get("rain") != null) {
                rain = Double.parseDouble(String.valueOf(weather.get("rain")));
            }

            JSONObject iconObject = (JSONObject) ((JSONArray) weather.get("weather")).get(0);
            String icon = String.valueOf((iconObject.get("icon")));

            saveDailyWeatherJson(region, localDateTime, morningTemp, dayTemp, nightTemp, humidity, rain, windSpeed, icon);
        }
    }
    private void saveHourlyWeatherJson(Region region, LocalDateTime date, Double temp, Double humidity, String icon) {
        HourlyWeather hourlyWeather = hourlyWeatherRepository.findByDateAndRegion(date, region);
        if (hourlyWeather == null) {
            hourlyWeather = HourlyWeather.builder().date(date).temp(temp)
                    .humidity(humidity).icon(icon).region(region).build();
        } else {
            hourlyWeather.from(temp, humidity, icon);
        }
        hourlyWeatherRepository.save(hourlyWeather);
    }
    private void saveDailyWeatherJson(Region region, LocalDateTime localDateTime, Double morningTemp, Double dayTemp, Double nightTemp, Double humidity, Double rain, Double windSpeed, String icon) {
        List<DailyWeather> dailyWeathers = dailyWeatherRepository.findByDateAndRegion(localDateTime, region);
        if (dailyWeathers.isEmpty()) {
            dailyWeatherRepository.save(DailyWeather.builder().date(localDateTime).avgTemp(morningTemp)
                    .windSpeed(windSpeed).rain(rain).humidity(humidity).timeOfDay(TimeOfDay.MORNING)
                    .region(region).icon(icon).build());
            dailyWeatherRepository.save(DailyWeather.builder().date(localDateTime).avgTemp(dayTemp)
                    .windSpeed(windSpeed).rain(rain).humidity(humidity).timeOfDay(TimeOfDay.AFTERNOON)
                    .region(region).icon(icon).build());
            dailyWeatherRepository.save(DailyWeather.builder().date(localDateTime).avgTemp(nightTemp)
                    .windSpeed(windSpeed).rain(rain).humidity(humidity).timeOfDay(TimeOfDay.NIGHT)
                    .region(region).icon(icon).build());
        } else {
            for (DailyWeather dailyWeather : dailyWeathers) {
                switch (dailyWeather.getTimeOfDay()) {
                    case MORNING:
                        dailyWeather.from(morningTemp, windSpeed, rain, humidity, icon);
                        break;
                    case AFTERNOON:
                        dailyWeather.from(dayTemp, windSpeed, rain, humidity, icon);
                        break;
                    case NIGHT:
                        dailyWeather.from(nightTemp, windSpeed, rain, humidity, icon);
                        break;
                }
                dailyWeatherRepository.saveAll(dailyWeathers);
            }
        }
    }
    @Transactional
    public ResponseEntity<HourlyWeatherResponse> getHourlyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withMinute(0).withSecond(0).withNano(0);

        Region region = memberRepository.findById(userId).
                map(m -> regionRepository.findByName(m.getRegion()))
                .orElse(regionRepository.findByName("서울특별시"));

        HourlyWeather hourlyWeather = hourlyWeatherRepository.findByDateAndRegion(localDateTime, region);

        return ResponseEntity.ok(new HourlyWeatherResponse().of(hourlyWeather));
    }
    @Transactional
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0);

        Region region = memberRepository.findById(userId).
                map(m -> regionRepository.findByName(m.getRegion()))
                .orElse(regionRepository.findByName("서울특별시"));

        List<DailyWeather> dailyWeathers = dailyWeatherRepository.findByDateAndRegion(localDateTime, region);

        Double highestTemp = dailyWeathers.stream()
                .filter(dailyWeather -> dailyWeather.getTimeOfDay() == TimeOfDay.AFTERNOON)
                .mapToDouble(DailyWeather::getAvgTemp)
                .findFirst()
                .orElse(0.0);

        Double lowestTemp = dailyWeathers.stream()
                .filter(dailyWeather -> dailyWeather.getTimeOfDay() == TimeOfDay.NIGHT)
                .mapToDouble(DailyWeather::getAvgTemp)
                .findFirst()
                .orElse(0.0);

        return ResponseEntity.ok(new DailyWeatherResponse().of(highestTemp, lowestTemp));
    }

    public LocalDateTime unixToDate(Long unixTime){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), TimeZone.getDefault().toZoneId());
    }
}
