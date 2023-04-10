package com.seungah.todayclothes.controller;

import com.seungah.todayclothes.dto.request.ScheduleRequest;
import com.seungah.todayclothes.dto.response.ScheduleResponse;
import com.seungah.todayclothes.service.ScheduleService;
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

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@AuthenticationPrincipal Long userId, @RequestBody ScheduleRequest scheduleRequest) {
        return scheduleService.createSchedule(userId, scheduleRequest);
    }

    @GetMapping
    public ResponseEntity <List<ScheduleResponse>> getSchedules(@AuthenticationPrincipal Long userId) {
        return scheduleService.getSchedules(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@AuthenticationPrincipal Long userId,
                                                           @RequestBody ScheduleRequest scheduleRequest,
                                                           @PathVariable Long id) {
        return scheduleService.updateSchedule(userId,scheduleRequest,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteSchedule(@AuthenticationPrincipal Long userId, @PathVariable Long id) {
        return scheduleService.deleteSchedule(userId,id);
    }

}
