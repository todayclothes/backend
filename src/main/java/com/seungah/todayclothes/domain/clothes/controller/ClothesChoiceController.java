package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.request.ChoiceClothesRequest;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesChoiceService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clothes/choice")
public class ClothesChoiceController {

	private final ClothesChoiceService clothesChoiceService;

	@PostMapping
	public ResponseEntity<Void> choiceClothesOfSchedule(@AuthenticationPrincipal Long userId,
		@RequestBody @Valid ChoiceClothesRequest request) {

		clothesChoiceService.choiceClothesOfSchedule(userId, request);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/mine")
	public ResponseEntity<Slice<ClothesChoiceResponse>> getUserClothesChoiceList(
		@RequestParam(required = false) Long lastClothesChoiceId,
		@AuthenticationPrincipal Long userId, Pageable pageable
	) {
		return ResponseEntity.ok(
			clothesChoiceService.getUserClothesChoiceList(
				userId, lastClothesChoiceId, pageable)
		);
	}

	@GetMapping("/others")
	public ResponseEntity<Slice<ClothesChoiceResponse>> getOtherUserClothesChoiceList(
		@RequestParam(required = false) Long lastClothesChoiceId,
		@AuthenticationPrincipal Long userId, Pageable pageable
	) {

		return ResponseEntity.ok(
			clothesChoiceService.getOtherUserClothesChoiceList(
				userId, lastClothesChoiceId, pageable)
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteChoiceClothes(@AuthenticationPrincipal Long userId,
		@PathVariable Long id) {

		clothesChoiceService.deleteClothesChoice(userId, id);

		return ResponseEntity.ok().build();
	}

}
