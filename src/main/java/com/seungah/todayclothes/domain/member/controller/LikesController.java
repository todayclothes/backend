package com.seungah.todayclothes.domain.member.controller;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.member.service.LikesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes/choice")
public class LikesController {

	private final LikesService likesService;

	@PostMapping("/{id}/like")
	public ResponseEntity<Void> createLike(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "id") Long clothesChoiceId
	){
		likesService.pressLikeOnClothesChoice(userId, clothesChoiceId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/like")
	public ResponseEntity<List<ClothesChoiceResponse>> getUserLikeList(
		@AuthenticationPrincipal Long userId // TODO page or slice
	){
		return ResponseEntity.ok(
			likesService.getUserLikeList(userId)
		);
	}

	@DeleteMapping("/like/{id}")
	public ResponseEntity<Void> deleteLike(
		@AuthenticationPrincipal Long userId,
		@PathVariable(name = "id") Long clothesChoiceId
	){
		likesService.cancelLikeOnClothesChoice(userId, clothesChoiceId);

		return ResponseEntity.ok().build();
	}

}
