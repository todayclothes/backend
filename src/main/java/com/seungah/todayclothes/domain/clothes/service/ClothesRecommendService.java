package com.seungah.todayclothes.domain.clothes.service;


import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;
import static com.seungah.todayclothes.global.type.TimeOfDay.AFTERNOON;
import static com.seungah.todayclothes.global.type.TimeOfDay.MORNING;
import static com.seungah.todayclothes.global.type.TimeOfDay.NIGHT;

import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesRecommendResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesRecommendResponse.Period;
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
import com.seungah.todayclothes.global.type.Plan;
import com.seungah.todayclothes.global.type.TimeOfDay;
import com.seungah.todayclothes.global.type.UserStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ClothesRecommendService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final DailyWeatherRepository dailyWeatherRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;

    private final ClothesService clothesService;
    private final AiService aiService;


    private static final int WEATHER_THRESHOLD = 22;
    private static final Integer DEFAULT_SPRING_TOP = 1;
    private static final Integer DEFAULT_SPRING_BOTTOM = 51;
    private static final Integer DEFAULT_SUMMER_TOP = 2;
    private static final Integer DEFAULT_SUMMER_BOTTOM = 52;

    private static final String DEFAULT_REGION = "서울특별시";

//    @Cacheable(value = "recommend", key = "#userId + ':' + #date")
    public ClothesRecommendResponse getClothesRecommend(Long userId, LocalDate date) {
        ClothesRecommendResponse response = new ClothesRecommendResponse(date);

        // member not login
        if (userId == null) {
            return getClothesForNotLogin(response);
        }

        Member member = memberRepository.getReferenceById(userId);

        // InActive Member
        if (member.getUserStatus() == UserStatus.INACTIVE) {
            return getClothesForNotLogin(response);
        }

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
            ClothesDto clothesDto = clothesService.getRecommendClothesDto(
                scheduleDetail.getTopClothesGroup(), scheduleDetail.getBottomClothesGroup(),
                scheduleDetail.getPlan(), member
            );

            setPeriod(Period.of(ScheduleDetailDto.of(scheduleDetail), clothesDto),
                scheduleDetail.getTimeOfDay(), response);
        }

        checkNullPeriod(response, member);

        return response;

    }

    private ClothesRecommendResponse getClothesForNotLogin(ClothesRecommendResponse response) {

        DailyWeather dailyWeather = dailyWeatherRepository.
            findByDateAndRegion(
                response.getDate().atTime(0,0), regionRepository.findByName(DEFAULT_REGION))
            .orElseThrow(() -> new CustomException(NOT_FOUND_DAILY_WEATHER));

        Map<TimeOfDay, Double> avgTemps = dailyWeather.getAvgTemps();
        for (TimeOfDay timeOfDay : avgTemps.keySet()) {
            Double avgTemp = avgTemps.get(timeOfDay);

            ClothesDto clothesDto;
            if (avgTemp > WEATHER_THRESHOLD) {
                clothesDto = clothesService.getClothesDto(
                    DEFAULT_SUMMER_TOP, DEFAULT_SUMMER_BOTTOM
                );
            } else {
                clothesDto = clothesService.getClothesDto(
                    DEFAULT_SPRING_TOP, DEFAULT_SPRING_BOTTOM
                );
            }

            setPeriod(Period.of(clothesDto), timeOfDay, response);

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
                Plan.DATE);

            ClothesDto clothesDto = clothesService.getRecommendClothesDto(
                aiClothesDto.getTopGroup(), aiClothesDto.getBottomGroup(),
                Plan.DATE, member
            );

            setPeriod(Period.of(clothesDto), timeOfDay, response);
        }

        return response;
    }

    private void checkNullPeriod(ClothesRecommendResponse response, Member member) {
        if (response.getMorning() == null) {
            ClothesDto clothesDto = getAiDefaultClothesDto(member, MORNING);

            Period period = Period.of(clothesDto);
            response.setMorning(period);
        }

        if (response.getAfternoon() == null) {
            ClothesDto clothesDto = getAiDefaultClothesDto(member, AFTERNOON);

            Period period = Period.of(clothesDto);
            response.setAfternoon(period);
        }

        if (response.getNight() == null) {
            ClothesDto clothesDto = getAiDefaultClothesDto(member, NIGHT);

            Period period = Period.of(clothesDto);
            response.setNight(period);
        }
    }

    private ClothesDto getAiDefaultClothesDto(Member member, TimeOfDay timeOfDay) {
        AiClothesDto aiClothesDto = aiService.callAiClothesApi(
            LocalDate.now(),
            timeOfDay,
            member.getRegion(),
            member.getGender().getType(),
            Plan.DATE);

        return clothesService.getRecommendClothesDto(
            aiClothesDto.getTopGroup(), aiClothesDto.getBottomGroup(),
            Plan.DATE, member
        );
    }

    private void setPeriod(
        Period period, TimeOfDay timeOfDay, ClothesRecommendResponse response
    ) {
        if (timeOfDay == MORNING) {
            response.setMorning(period);
        } else if (timeOfDay == AFTERNOON) {
            response.setAfternoon(period);
        } else if (timeOfDay == NIGHT) {
            response.setNight(period);
        }
    }

}
