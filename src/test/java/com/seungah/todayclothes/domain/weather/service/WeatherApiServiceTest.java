package com.seungah.todayclothes.domain.weather.service;

import static org.junit.jupiter.api.Assertions.*;

import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class WeatherApiServiceTest {
	@Autowired
	private WeatherService weatherService;
	@Autowired
	private RegionRepository regionRepository;
	@Autowired
	private WeatherApiService weatherApiService;

	@Test
	@Transactional
	@Rollback(value = false)
	public void hourlyWeatherJsonParsingTest(){
		weatherApiService.saveWeather();
	}

}