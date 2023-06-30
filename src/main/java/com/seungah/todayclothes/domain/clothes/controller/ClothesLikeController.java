package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.response.BottomLikeResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopLikeResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Slice<TopLikeResponse>> getTopLike(
        @RequestParam(required = false) Long lastTopLikeId,
        @AuthenticationPrincipal Long userId, Pageable pageable
    ) {
        return ResponseEntity.ok(
            clothesLikeService.getTopLike(userId, lastTopLikeId, pageable)
        );
    }

    @GetMapping("/bottom")
    public ResponseEntity<Slice<BottomLikeResponse>> getBottomLike(
        @RequestParam(required = false) Long lastBottomLikeId,
        @AuthenticationPrincipal Long userId, Pageable pageable
    ) {
        return ResponseEntity.ok(
            clothesLikeService.getBottomLike(userId, lastBottomLikeId, pageable)
        );
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
