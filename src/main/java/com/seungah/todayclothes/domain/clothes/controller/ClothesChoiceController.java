package com.seungah.todayclothes.domain.clothes.controller;

import com.seungah.todayclothes.domain.clothes.dto.request.ChoiceClothesRequest;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.clothes.service.ClothesChoiceService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ResponseEntity<List<ClothesChoiceResponse>> getUserClothesChoiceList(@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(clothesChoiceService.getUserClothesChoiceList(userId));
	}

	@GetMapping("/others")
	public ResponseEntity<List<ClothesChoiceResponse>> getOtherUserClothesChoiceList(@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(clothesChoiceService.getOtherUserClothesChoiceList(userId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteChoiceClothes(@AuthenticationPrincipal Long userId,
		@PathVariable Long id) {

		clothesChoiceService.deleteClothesChoice(userId, id);

		return ResponseEntity.ok().build();
	}

}
