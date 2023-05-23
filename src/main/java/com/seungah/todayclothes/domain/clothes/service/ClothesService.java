package com.seungah.todayclothes.domain.clothes.service;


import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.seungah.todayclothes.global.type.TimeOfDay.AFTERNOON;
import static com.seungah.todayclothes.global.type.TimeOfDay.MORNING;
import static com.seungah.todayclothes.global.type.TimeOfDay.NIGHT;

import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesRecommendResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesRecommendResponse.Period;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.schedule.dto.ScheduleDetailDto;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleDetailRepository;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleRepository;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.ai.service.AiService;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;
    private final RegionRepository regionRepository;
    private final DailyWeatherRepository dailyWeatherRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;

    private final AiService aiService;


    private final int WEATHER_THRESHOLD = 22;
    private final Integer DEFAULT_SPRING_TOP = 1;
    private final Integer DEFAULT_SPRING_BOTTOM = 51;
    private final Integer DEFAULT_SUMMER_TOP = 2;
    private final Integer DEFAULT_SUMMER_BOTTOM = 52;
    private final String DEFAULT_PLAN = "데이트";

    @Cacheable(value = "recommend", key = "#userId + ':' + #date")
    public ClothesRecommendResponse getClothesRecommend(Long userId, LocalDate date) {
        ClothesRecommendResponse response = new ClothesRecommendResponse(date);

        // member not login
        if (userId == null) {
            return getNotLoginClothes(response);
        }

        // member check
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        // schedule get
        Schedule schedule = scheduleRepository.findByMemberAndDate(member, date);
        if (schedule == null) {
            schedule = Schedule.createSchedule(member, date);
            scheduleRepository.save(schedule);
            return getClothesForNoSchedule(response, member);
        }

        // schedule detail get
        List<ScheduleDetail> scheduleDetailList =
            scheduleDetailRepository.findAllBySchedule(schedule);

        // clothes get - 스케줄 없을 때,
        if (CollectionUtils.isEmpty(scheduleDetailList)) {
            return getClothesForNoSchedule(response, member);
        }

        // clothes get - 스케줄 있을 때,
        for (ScheduleDetail scheduleDetail: scheduleDetailList) {

            List<TopDto> topList = getTopClothes(scheduleDetail.getTopClothesGroup());
            List<BottomDto> bottomList = getBottomClothes(scheduleDetail.getBottomClothesGroup());
            ClothesDto clothesDto = ClothesDto.of(topList, bottomList);

            Period period = Period.of(ScheduleDetailDto.of(scheduleDetail), clothesDto);
            if (scheduleDetail.getTimeOfDay() == MORNING) {
                response.setMorning(period);
            } else if (scheduleDetail.getTimeOfDay() == AFTERNOON) {
                response.setAfternoon(period);
            } else if (scheduleDetail.getTimeOfDay() == NIGHT) {
                response.setNight(period);
            }

        }

        if (response.getMorning() == null) {
            AiClothesDto aiClothesDto = aiService.callAiClothesApi(
                LocalDate.now(),
                MORNING,
                member.getRegion(),
                member.getGender().getType(),
                DEFAULT_PLAN);

            List<TopDto> topDtoList = getTopClothes(aiClothesDto.getTopGroup());
            List<BottomDto> bottomDtoList = getBottomClothes(aiClothesDto.getBottomGroup());
            ClothesDto clothesDto = ClothesDto.of(topDtoList, bottomDtoList);

            Period period = Period.of(clothesDto);
            response.setMorning(period);
        }

        if (response.getAfternoon() == null) {
            AiClothesDto aiClothesDto = aiService.callAiClothesApi(
                LocalDate.now(),
                AFTERNOON,
                member.getRegion(),
                member.getGender().getType(),
                DEFAULT_PLAN);

            List<TopDto> topDtoList = getTopClothes(aiClothesDto.getTopGroup());
            List<BottomDto> bottomDtoList = getBottomClothes(aiClothesDto.getBottomGroup());
            ClothesDto clothesDto = ClothesDto.of(topDtoList, bottomDtoList);

            Period period = Period.of(clothesDto);
            response.setAfternoon(period);
        }

        if (response.getNight() == null) {
            AiClothesDto aiClothesDto = aiService.callAiClothesApi(
                LocalDate.now(),
                NIGHT,
                member.getRegion(),
                member.getGender().getType(),
                DEFAULT_PLAN);

            List<TopDto> topDtoList = getTopClothes(aiClothesDto.getTopGroup());
            List<BottomDto> bottomDtoList = getBottomClothes(aiClothesDto.getBottomGroup());
            ClothesDto clothesDto = ClothesDto.of(topDtoList, bottomDtoList);

            Period period = Period.of(clothesDto);
            response.setNight(period);
        }

        return response;

    }

    public ClothesRecommendResponse getNotLoginClothes(ClothesRecommendResponse response) {

        DailyWeather dailyWeather = dailyWeatherRepository.
            findByDateAndRegion(
                LocalDateTime.of(response.getDate(), LocalTime.of(12,0,0)),
                regionRepository.findByName("서울특별시"))
            .orElseThrow(() -> new CustomException(NOT_FOUND_DAILY_WEATHER));

        List<TopDto> topDtoList;
        List<BottomDto> bottomDtoList;
        Map<TimeOfDay, Double> avgTemps = dailyWeather.getAvgTemps();

        for (TimeOfDay timeOfDay : avgTemps.keySet()) {
            Double avgTemp = avgTemps.get(timeOfDay);

            if (avgTemp > WEATHER_THRESHOLD) {
                topDtoList = getTopClothes(DEFAULT_SUMMER_TOP);
                bottomDtoList = getBottomClothes(DEFAULT_SUMMER_BOTTOM);
            } else {
                topDtoList = getTopClothes(DEFAULT_SPRING_TOP);
                bottomDtoList = getBottomClothes(DEFAULT_SPRING_BOTTOM);
            }

            Period period = Period.of(ClothesDto.of(topDtoList, bottomDtoList));
            if (timeOfDay == MORNING) {
                response.setMorning(period);
            } else if (timeOfDay == AFTERNOON) {
                response.setAfternoon(period);
            } else if (timeOfDay == NIGHT) {
                response.setNight(period);
            }

        }

        return response;
    }

    private ClothesRecommendResponse getClothesForNoSchedule(
        ClothesRecommendResponse response, Member member
    ) {

        for (TimeOfDay timeOfDay : TimeOfDay.values()) {
            AiClothesDto aiClothesDto = aiService.callAiClothesApi(
                LocalDate.now(),
                timeOfDay,
                member.getRegion(),
                member.getGender().getType(),
                DEFAULT_PLAN);

            List<TopDto> topDtoList = getTopClothes(aiClothesDto.getTopGroup());
            List<BottomDto> bottomDtoList = getBottomClothes(aiClothesDto.getBottomGroup());
            ClothesDto clothesDto = ClothesDto.of(topDtoList, bottomDtoList);

            Period period = Period.of(clothesDto);
            if (timeOfDay == MORNING) {
                response.setMorning(period);
            } else if (timeOfDay == AFTERNOON) {
                response.setAfternoon(period);
            } else if (timeOfDay == NIGHT) {
                response.setNight(period);
            }

        }

        return response;
    }

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

}
