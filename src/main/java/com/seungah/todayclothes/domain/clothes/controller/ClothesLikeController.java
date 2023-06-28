package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.response.BottomLikeResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopLikeResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes/like")
public class ClothesLikeController {
    private final ClothesLikeService clothesLikeService;

    @PostMapping("/top/{id}")
    public ResponseEntity<Void> pressTopLike(@AuthenticationPrincipal Long userId,
        @PathVariable(name = "id") Long topId
    ) {
        clothesLikeService.pressTopLike(userId, topId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bottom/{id}")
    public ResponseEntity<Void> pressBottomLike(@AuthenticationPrincipal Long userId,
        @PathVariable(name = "id") Long bottomId
    ) {
        clothesLikeService.pressBottomLike(userId, bottomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopLikeResponse>> getTopLike(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(clothesLikeService.getTopLike(userId));
    }

    @GetMapping("/bottom")
    public ResponseEntity<List<BottomLikeResponse>> getBottomLike(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(clothesLikeService.getBottomLike(userId));
    }

    @DeleteMapping("/top/{id}")
    public ResponseEntity<Void> cancelTopLike(@AuthenticationPrincipal Long userId,
            @PathVariable(name = "id") Long topId
    ) {
        clothesLikeService.cancelTopLike(userId, topId);

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/bottom/{id}")
    public ResponseEntity<Void> cancelBottomLike(@AuthenticationPrincipal Long userId,
            @PathVariable(name = "id") Long bottomId
    ) {
        clothesLikeService.cancelBottomLike(userId, bottomId);

        return ResponseEntity.ok().build();
    }

}
