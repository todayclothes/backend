package com.seungah.todayclothes.domain.schedule.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_REGION;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_SCHEDULE_DETAIL;

import com.seungah.todayclothes.domain.clothes.repository.ClothesChoiceRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.schedule.dto.request.AddScheduleRequest;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleDetailRepository;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleRepository;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.ai.dto.AiScheduleDto;
import com.seungah.todayclothes.global.ai.service.AiService;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.TimeOfDay;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final ClothesChoiceRepository clothesChoiceRepository;

    private final AiService aiService;
    private final CacheManager cacheManager;


    @Transactional
//    @CacheEvict(value = "recommend", key = "#userId + ':' + #request.date")
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
        String regionName = request.getRegionName();
        if (regionName == null) { // region이 없을 시,
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

    @Transactional
    public void deleteScheduleDetail(Long userId, Long id) {

        Member member = memberRepository.getReferenceById(userId);

        ScheduleDetail scheduleDetail = scheduleDetailRepository.findById(id)
            .orElseThrow(() -> new CustomException(NOT_FOUND_SCHEDULE_DETAIL)
        );

        // 옷 선택 있으면 지우기
        clothesChoiceRepository.deleteAllByScheduleDetail(scheduleDetail);

        scheduleDetailRepository.delete(scheduleDetail);

//        LocalDate date = scheduleDetail.getSchedule().getDate();
//        Objects.requireNonNull(cacheManager.getCache("recommend"))
//            .evict(userId + ":" + date);

    }

}
