package com.seungah.todayclothes.domain.member.controller;

import com.seungah.todayclothes.domain.member.dto.request.CheckAuthNumberRequest;
import com.seungah.todayclothes.domain.member.dto.request.FindPasswordRequest;
import com.seungah.todayclothes.domain.member.dto.request.SendAuthNumberRequest;
import com.seungah.todayclothes.domain.member.dto.request.UpdateMemberInfoRequest;
import com.seungah.todayclothes.domain.member.dto.request.UpdateNameRequest;
import com.seungah.todayclothes.domain.member.dto.request.UpdatePasswordRequest;
import com.seungah.todayclothes.domain.member.dto.response.CheckAuthNumberResponse;
import com.seungah.todayclothes.domain.member.dto.response.GetProfileResponse;
import com.seungah.todayclothes.domain.member.service.MemberService;
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

	@PutMapping("/active-info")
	public ResponseEntity<?> updateActiveMemberInfo(
		@Valid @RequestBody UpdateMemberInfoRequest request,
		@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(
			memberService.updateActiveMemberInfo(
				userId, request.getRegionId(), request.getGender())
		);
	}

	@PostMapping("/phone/auth-num")
	public ResponseEntity<Void> sendAuthNumberBySms(@Valid @RequestBody SendAuthNumberRequest request,
		@AuthenticationPrincipal Long userId) {

		memberService.sendAuthNumberBySms(userId, request.getPhone());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/phone/auth-num/check")
	public ResponseEntity<CheckAuthNumberResponse> checkAuthNumberAndSavePhone(
		@Valid @RequestBody CheckAuthNumberRequest request,
		@AuthenticationPrincipal Long userId
	) {

		return ResponseEntity.ok(
			memberService.checkAuthNumberAndSavePhone(
				userId, request.getAuthNumber(), request.getPhone()));
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
