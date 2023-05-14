package com.seungah.todayclothes.global.ai.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.FAILED_CALL_AI_API;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.ai.dto.AiScheduleDto;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AiService {

	private final DailyWeatherRepository dailyWeatherRepository;

	@Value("${ai.schedule}")
	private String scheduleUrl;
	@Value("${ai.clothes}")
	private String clothesUrl;

	public AiScheduleDto callAiScheduleApi(String title, String region) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(scheduleUrl)
			.queryParam("title", title)
			.queryParam("region", region);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(
			StandardCharsets.UTF_8));
		String response = restTemplate.getForObject(builder.toUriString(), String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = objectMapper.readTree(response);
		} catch (JsonProcessingException e) {
			throw new CustomException(FAILED_CALL_AI_API);
		}
		String plan = rootNode.get("plan").asText();
		String regionName = rootNode.get("region").asText();

		return AiScheduleDto.of(plan, regionName);
	}

	public AiClothesDto callAiClothesApi(LocalDate date, TimeOfDay timeOfDay,
		Region region, String genderType, String plan) {

		DailyWeather dailyWeather = dailyWeatherRepository
			.findByDateAndRegion(date.atTime(0, 0), region)
			.orElseThrow(() -> new CustomException(NOT_FOUND_DAILY_WEATHER));

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(clothesUrl)
			.queryParam("gender", genderType)
			.queryParam("humidity", dailyWeather.getHumidity())
			.queryParam("wind_speed", dailyWeather.getWindSpeed())
			.queryParam("rain", dailyWeather.getRain())
			.queryParam("temp", dailyWeather.getAvgTemps().get(timeOfDay))
			.queryParam("schedule", plan);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(
			StandardCharsets.UTF_8));
        String response = restTemplate.getForObject(builder.toUriString(), String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode2 = null;
		try {
			rootNode2 = objectMapper.readTree(response);
		} catch (JsonProcessingException e) {
			throw new CustomException(FAILED_CALL_AI_API);
		}
		String top = rootNode2.get("top").asText();
		String bottom = rootNode2.get("bottom").asText();

		return AiClothesDto.of(top, bottom);
	}
}
