package com.seungah.todayclothes.domain.schedule.service;

import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import com.seungah.todayclothes.domain.schedule.dto.request.ScheduleRequest;
import com.seungah.todayclothes.domain.schedule.dto.response.ScheduleResponse;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<ScheduleResponse> createSchedule(Long userId, ScheduleRequest scheduleRequest) {

        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Schedule schedule = scheduleRepository.save(Schedule.of(scheduleRequest,member));
        return ResponseEntity.ok(ScheduleResponse.of(schedule));
    }

    @Transactional
    public ResponseEntity<List<ScheduleResponse>> getSchedules(Long userId) {
        //TODO Month, day 등 분류 어떤 방식으로 불러오는지 로직 구현
        List<Schedule> scheduleList = scheduleRepository.findByMemberId(userId);
        List<ScheduleResponse> scheduleResponsesList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            scheduleResponsesList.add(ScheduleResponse.of(schedule));
        }
        return ResponseEntity.ok(scheduleResponsesList);
    }

    @Transactional
    public ResponseEntity<ScheduleResponse> updateSchedule(Long userId, ScheduleRequest scheduleRequest, Long id) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        schedule.update(scheduleRequest);

        ScheduleResponse scheduleResponse = ScheduleResponse.of(schedule);

        return ResponseEntity.ok(scheduleResponse);
    }

    @Transactional
    public ResponseEntity<Void> deleteSchedule(Long userId, Long id) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        scheduleRepository.delete(schedule);
        return ResponseEntity.ok().build();
    }

}
