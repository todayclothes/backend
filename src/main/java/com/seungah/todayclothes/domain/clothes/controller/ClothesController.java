package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesRecommendResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesRecommendService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes")
public class ClothesController {
    private final ClothesRecommendService clothesRecommendService;

    @GetMapping("/recommend")
    public ResponseEntity<ClothesRecommendResponse> getClothesRecommend(
        @AuthenticationPrincipal Long userId,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
            clothesRecommendService.getClothesRecommend(userId, date));
    }

}