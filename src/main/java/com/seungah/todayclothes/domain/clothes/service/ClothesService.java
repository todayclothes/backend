package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;
    private final RegionRepository regionRepository;
    private final DailyWeatherRepository dailyWeatherRepository;

    private final Double weatherThreshold = 45.;
    private final Integer defaultSpringTop = 1;
    private final Integer defaultSpringBottom = 51;
    private final Integer defaultSummerTop = 2;
    private final Integer defaultSummerBottom = 52;

    public List<TopDto> getTopClothes(Integer groupNumber) {
        List<TopDto> topDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<Top> topList = topRepository.findRandomEntitiesByClothesGroup(
            clothesGroupRepository.findByGroupNumber(groupNumber)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);
        for (Top top : topList) {
            topDtoList.add(TopDto.of(top, groupNumber));
        }
        Collections.shuffle(topDtoList);
        return topDtoList;
    }

    public List<BottomDto> getBottomClothes(Integer groupNumber) {
        List<BottomDto> bottomDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<Bottom> bottomList = bottomRepository.findRandomEntitiesByClothesGroup(
            clothesGroupRepository.findByGroupNumber(groupNumber)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);
        for (Bottom bottom : bottomList) {
            bottomDtoList.add(BottomDto.of(bottom, groupNumber));
        }
        Collections.shuffle(bottomDtoList);
        return bottomDtoList;
    }
    public ClothesWithScheduleResponse getNotLoginClothes(LocalDate date) {
        Optional<DailyWeather> dailyWeather = dailyWeatherRepository.
                findByDateAndRegion(LocalDateTime.of(date, LocalTime.of(12,0,0)),
                        regionRepository.findByName("서울특별시"));
        List<TopDto> topDtoList;
        List<BottomDto> bottomDtoList;
        if (dailyWeather.get().getHighTemp() + dailyWeather.get().getLowTemp() > weatherThreshold) {
            topDtoList = getTopClothes(defaultSpringTop);
            bottomDtoList = getBottomClothes(defaultSpringBottom);
        } else {
            topDtoList = getTopClothes(defaultSummerTop);
            bottomDtoList = getBottomClothes(defaultSummerBottom);
        }
        ClothesDto clothesDto = ClothesDto.of(topDtoList, bottomDtoList);
        return new ClothesWithScheduleResponse(clothesDto);
    }
}
