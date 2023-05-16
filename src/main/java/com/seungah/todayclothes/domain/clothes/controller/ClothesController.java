package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.service.ClothesService;
import com.seungah.todayclothes.domain.schedule.dto.response.ClothesWithScheduleResponse;
import com.seungah.todayclothes.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes")
public class ClothesController {
    private final ClothesService clothesService;
    private final ScheduleService scheduleService;

    // 스케쥴 리스트 조회
    @GetMapping
    public ResponseEntity<ClothesWithScheduleResponse> getClothesWithSchedules(
        @AuthenticationPrincipal Long userId,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
    ) {
        if (userId == null) {
            return ResponseEntity.ok(
                    clothesService.getNotLoginClothes(date));
        }
        return ResponseEntity.ok(
                scheduleService.getClothesBySchedule(userId, date));

    }


    /*
    @GetMapping("/top")
    public ResponseEntity<List<TopResponse>> getTopClothes(Integer groupNumber) {
        return clothesService.getTopClothes(groupNumber);
    }
    @GetMapping("/bottom")
    public ResponseEntity<List<BottomResponse>> getBottomClothes(Integer groupNumber) {
        return clothesService.getBottomClothes(groupNumber);
    }
    */
}