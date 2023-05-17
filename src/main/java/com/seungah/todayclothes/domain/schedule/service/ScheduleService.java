package com.seungah.todayclothes.domain.schedule.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_REGION;
import static com.seungah.todayclothes.global.type.TimeOfDay.AFTERNOON;

import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.repository.ClothesChoiceRepository;
import com.seungah.todayclothes.domain.clothes.service.ClothesService;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.schedule.dto.request.AddScheduleRequest;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse.ScheduleDto;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse.ScheduleDto.ScheduleDetailDto;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleDetailRepository;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleRepository;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.ai.dto.AiScheduleDto;
import com.seungah.todayclothes.global.ai.service.AiService;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final ClothesChoiceRepository clothesChoiceRepository;

    private final AiService aiService;
    private final ClothesService clothesService;

    private final CacheManager cacheManager;


    @Transactional
    @CacheEvict(value = "schedules", key = "#userId + ':' + #request.date")
    public void addSchedule(Long userId, AddScheduleRequest request) {

        // member check
        Member member = memberRepository.getReferenceById(userId);

        // member schedule get
        Schedule schedule = scheduleRepository.findByMemberAndDate(member, request.getDate());
        if (schedule == null) {
            schedule = Schedule.createSchedule(member, request.getDate());
            scheduleRepository.save(schedule);
        }

        // call ai service
        // - plan과 region을 받아 오기 위한 모델 요청
        String regionName = "";
        if (request.getRegionName() == null) { // region이 없을 시,
            regionName = member.getRegion().getName();
        }
        AiScheduleDto aiScheduleDto =
            aiService.callAiScheduleApi(request.getTitle(), regionName);

        String processedRegion = aiScheduleDto.getRegion();
        Region region = regionRepository.findByName(processedRegion);
        if (region == null) throw new CustomException(NOT_FOUND_REGION);

        // - 옷 그룹을 받아오기 위해 의류 모델 API 요청
        AiClothesDto aiClothesDto =
            aiService.callAiClothesApi(request.getDate(), TimeOfDay.valueOf(request.getTimeOfDay()),
                region, member.getGender().getType(), aiScheduleDto.getPlan());

        // Save & update 일정 세부사항
        ScheduleDetail scheduleDetail = scheduleDetailRepository
            .findByScheduleAndTimeOfDay(schedule, TimeOfDay.valueOf(request.getTimeOfDay()));
        if (scheduleDetail == null) {
            scheduleDetail = ScheduleDetail.createScheduleDetail(request,
                aiScheduleDto.getPlan(), region, aiClothesDto, schedule);
            scheduleDetailRepository.save(scheduleDetail);
        } else {
            // 옷 선택있으면 지우기
            clothesChoiceRepository.deleteAllByScheduleDetail(scheduleDetail);

            scheduleDetail.updateScheduleDetail(request,
                aiScheduleDto.getPlan(), region, aiClothesDto);
        }

    }

    @Cacheable(value = "schedules", key = "#userId + ':' + #date")
    public ClothesWithScheduleResponse getClothesBySchedule(
        Long userId, LocalDate date
    ) {
        // member check
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        // schedule get
        Schedule schedule = scheduleRepository.findByMemberAndDate(member, date);
        if (schedule == null) {
            schedule = Schedule.createSchedule(member, date);
            scheduleRepository.save(schedule);
        }

        // schedule detail get
        List<ScheduleDetail> scheduleDetailList =
            scheduleDetailRepository.findAllBySchedule(schedule);

        // clothes get - 스케줄 없을 때
        if (CollectionUtils.isEmpty(scheduleDetailList)) {

            ClothesDto clothesDto = getClothesForNoSchedule(member);

            return new ClothesWithScheduleResponse(clothesDto);
        }

        // clothes get - 스케줄 있을 때
        List<ScheduleDetailDto> scheduleDetailDtoList = new ArrayList<>();
        for (ScheduleDetail scheduleDetail: scheduleDetailList) {

            List<TopDto> topList = clothesService.getTopClothes(
                scheduleDetail.getTopClothesGroup());
            List<BottomDto> bottomList = clothesService.getBottomClothes(
                scheduleDetail.getBottomClothesGroup());
            ClothesDto clothesDto = ClothesDto.of(topList, bottomList);

            scheduleDetailDtoList.add(
                ScheduleDetailDto.of(scheduleDetail, clothesDto));
        }

        return new ClothesWithScheduleResponse(
            ScheduleDto.of(schedule, scheduleDetailDtoList));
    }

    @Transactional
    public void deleteScheduleDetail(Long userId, Long id) {

        Member member = memberRepository.getReferenceById(userId);

        ScheduleDetail scheduleDetail = scheduleDetailRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE_DETAIL)
        );

        // 옷 선택 있으면 지우기
        clothesChoiceRepository.deleteAllByScheduleDetail(scheduleDetail);

        scheduleDetailRepository.delete(scheduleDetail);

        LocalDate date = scheduleDetail.getSchedule().getDate();
        Objects.requireNonNull(cacheManager.getCache("schedules"))
            .evict(userId + ":" + date);

    }

    private ClothesDto getClothesForNoSchedule(Member member) {

        AiClothesDto aiClothesDto = aiService.callAiClothesApi(
            LocalDate.now(),
            AFTERNOON,
            member.getRegion(),
            member.getGender().getType(),
            "데이트");

        return ClothesDto.builder()
            .top(clothesService.getTopClothes(aiClothesDto.getTopGroup()))
            .bottom(clothesService.getBottomClothes(aiClothesDto.getBottomGroup()))
            .build();
    }

}
