package com.seungah.todayclothes.global.ai.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.FAILED_CALL_AI_API;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.ai.dto.AiScheduleDto;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.Plan;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiService {

	private final DailyWeatherRepository dailyWeatherRepository;

	@Value("${ai.schedule}")
	private String scheduleUrl;
	@Value("${ai.clothes}")
	private String clothesUrl;

	public AiScheduleDto callAiScheduleApi(String title, String region) {
		URI uri;
		try {
			uri = new URIBuilder(scheduleUrl)
				.addParameter("title", title)
				.addParameter("region", region)
				.build();
		} catch (Exception e) {
			throw new RuntimeException();
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(request);
			String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			String planKeyword = rootNode.get("plan").asText();
			Plan plan = Arrays.stream(Plan.values())
				.filter(p -> p.getKeyword().equals(planKeyword))
				.findFirst()
				.orElse(Plan.DATE);
			String regionName = rootNode.get("region").asText();

			return AiScheduleDto.of(plan, regionName);
		} catch (Exception e) {
			throw new CustomException(FAILED_CALL_AI_API);
		}
	}

	public AiClothesDto callAiClothesApi(LocalDate date, TimeOfDay timeOfDay,
		Region region, String genderType, Plan plan) {

		DailyWeather dailyWeather = dailyWeatherRepository
			.findByDateAndRegion(date.atTime(0, 0), region)
			.orElseThrow(() -> new CustomException(NOT_FOUND_DAILY_WEATHER));

		URI uri;
		try {
			uri = new URIBuilder(clothesUrl)
				.addParameter("gender", genderType)
				.addParameter("humidity", String.valueOf(dailyWeather.getHumidity()))
				.addParameter("wind_speed", String.valueOf(dailyWeather.getWindSpeed()))
				.addParameter("rain", String.valueOf(dailyWeather.getRain()))
				.addParameter("temp", String.valueOf(dailyWeather.getAvgTemps().get(timeOfDay)))
				.addParameter("schedule", plan.getKeyword())
				.build();
		} catch (Exception e) {
			throw new RuntimeException();
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(request);
			String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			String top = rootNode.get("top").asText();
			String bottom = rootNode.get("bottom").asText();

			return AiClothesDto.of(top, bottom, dailyWeather.getAvgTemps().get(timeOfDay));
		} catch (Exception e) {
			throw new CustomException(FAILED_CALL_AI_API);
		}
	}
}
