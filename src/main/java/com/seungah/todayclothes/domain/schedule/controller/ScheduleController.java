package com.seungah.todayclothes.domain.schedule.controller;

import com.seungah.todayclothes.domain.schedule.dto.request.AddScheduleRequest;
import com.seungah.todayclothes.domain.schedule.service.ScheduleService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 상세 스케줄 (및 스케줄) 생성
    @PostMapping
    public ResponseEntity<Void> addSchedule(@AuthenticationPrincipal Long userId,
        @RequestBody @Valid AddScheduleRequest request) {

        scheduleService.addSchedule(userId, request);

        return ResponseEntity.ok().build();
    }

    // 상세 스케쥴 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleDetail(@AuthenticationPrincipal Long userId,
        @PathVariable Long id) {

        scheduleService.deleteScheduleDetail(userId, id);

        return ResponseEntity.ok().build();
    }

}
