package com.seungah.todayclothes.controller;

import com.seungah.todayclothes.dto.request.FindPasswordRequest;
import com.seungah.todayclothes.dto.request.UpdatePasswordRequest;
import com.seungah.todayclothes.dto.request.UpdateGenderRequest;
import com.seungah.todayclothes.dto.request.UpdateNameRequest;
import com.seungah.todayclothes.dto.request.UpdateRegionRequest;
import com.seungah.todayclothes.dto.response.GetProfileResponse;
import com.seungah.todayclothes.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;
	@GetMapping("/profile")
	public ResponseEntity<GetProfileResponse> getProfile(@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(memberService.getProfile(userId));
	}

	@PutMapping("/gender")
	public ResponseEntity<GetProfileResponse> updateGender(@Valid @RequestBody UpdateGenderRequest request,
		@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(
			memberService.updateGender(userId, request.getGender())
		);
	}

	@PutMapping("/region")
	public ResponseEntity<GetProfileResponse> updateRegion(@Valid @RequestBody UpdateRegionRequest request,
		@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(
			memberService.updateRegion(userId, request.getRegion())
		);
	}

	@PatchMapping("/name")
	public ResponseEntity<Void> updateName(@Valid @RequestBody UpdateNameRequest request,
		@AuthenticationPrincipal Long userId) {

		memberService.updateName(userId, request.getName());

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/password")
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest request,
		@AuthenticationPrincipal Long userId) {

		memberService.updatePassword(userId, request.getPassword());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/password/find")
	public ResponseEntity<Void> findPassword(@Valid @RequestBody FindPasswordRequest request) {

		memberService.findPassword(request.getEmail());

		return ResponseEntity.ok().build();
	}
}