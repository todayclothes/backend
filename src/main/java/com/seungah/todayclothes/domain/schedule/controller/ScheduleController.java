package com.seungah.todayclothes.domain.schedule.controller;

import com.seungah.todayclothes.domain.schedule.dto.request.ScheduleRequest;
import com.seungah.todayclothes.domain.schedule.dto.response.ScheduleResponse;
import com.seungah.todayclothes.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 1. 스케쥴 생성.
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@AuthenticationPrincipal Long userId, @RequestBody ScheduleRequest scheduleRequest) {
        return scheduleService.createSchedule(userId, scheduleRequest);
    }

    // 2. 스케쥴 리스트 조회
    @GetMapping
    public ResponseEntity <List<ScheduleResponse>> getSchedules(@AuthenticationPrincipal Long userId) {
        return scheduleService.getSchedules(userId);
    }

    // 3. 단일 스케쥴 리조회
    @GetMapping
    public ResponseEntity <ScheduleResponse> getSchedule(@AuthenticationPrincipal Long userId) {
        return scheduleService.getSchedule(userId);
    }

    // 4. 스케쥴 수정
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@AuthenticationPrincipal Long userId,
                                                           @RequestBody ScheduleRequest scheduleRequest,
                                                           @PathVariable Long id) {
        return scheduleService.updateSchedule(userId,scheduleRequest,id);
    }

    // 5. 스케쥴 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteSchedule(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return scheduleService.deleteSchedule(userId,id);
    }

}
