package com.seungah.todayclothes.controller;

import com.seungah.todayclothes.dto.request.CheckAuthKeyRequest;
import com.seungah.todayclothes.dto.request.SendAuthKeyRequest;
import com.seungah.todayclothes.dto.request.SignUpRequest;
import com.seungah.todayclothes.dto.response.CheckAuthKeyResponse;
import com.seungah.todayclothes.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-up")
	public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
		authService.register(
			request.getEmail(), request.getPassword(), request.getName()
		);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/auth-key")
	public ResponseEntity<Void> sendAuthKeyByMail(@RequestBody @Valid SendAuthKeyRequest request) {
		authService.sendAuthKeyByMail(request.getEmail());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/email/auth-key/check")
	public ResponseEntity<CheckAuthKeyResponse> checkAuthKey(@RequestBody @Valid CheckAuthKeyRequest request) {
		return ResponseEntity.ok(
			authService.checkAuthKey(request.getEmail(), request.getAuthKey())
		);
	}

}
