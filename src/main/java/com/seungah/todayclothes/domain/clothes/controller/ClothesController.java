package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.response.BottomResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes")
public class ClothesController {
    private final ClothesService clothesService;

    @GetMapping("/top")
    public ResponseEntity<List<TopResponse>> getTopClothes(@AuthenticationPrincipal Long userId) {
        return clothesService.getTopClothes(userId);
    }
    @GetMapping("/bottom")
    public ResponseEntity<List<BottomResponse>> getBottomClothes(@AuthenticationPrincipal Long userId) {
        return clothesService.getBottomClothes(userId);
    }
}