package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.entity.ClothesGroupType;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupTypeRepository;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

@RequiredArgsConstructor
@Service
public class ClothesService {

    private final ClothesGroupRepository clothesGroupRepository;
    private final RegionRepository regionRepository;
    private final DailyWeatherRepository dailyWeatherRepository;
    private final ClothesGroupTypeRepository clothesGroupTypeRepository;
	private final ClothesGroupRepository clothesGroupRepository;

	public ClothesDto getClothesDto(Integer topGroupNumber, Integer bottomGroupNumber) {
		return ClothesDto.of(
			getTopClothes(topGroupNumber), getBottomClothes(bottomGroupNumber)
		);
	}

    public List<TopDto> getTopClothes(Integer groupNumber) {
        List<TopDto> topDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<ClothesGroupType> clothesGroupTypes = clothesGroupTypeRepository.findRandomEntitiesByClothesGroup(
                clothesGroupRepository.findByGroupNumber(groupNumber)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);

        for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
            topDtoList.add(TopDto.of(clothesGroupType.getTop(), groupNumber));
        }
        return topDtoList;
    }

    public List<BottomDto> getBottomClothes(Integer groupNumber) {
        List<BottomDto> bottomDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<ClothesGroupType> clothesGroupTypes = clothesGroupTypeRepository.findRandomEntitiesByClothesGroup(
                clothesGroupRepository.findByGroupNumber(groupNumber)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);

        for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
            bottomDtoList.add(BottomDto.of(clothesGroupType.getBottom(), groupNumber));
        }
        return bottomDtoList;
    }
}
