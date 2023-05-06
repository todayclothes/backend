package com.seungah.todayclothes.domain.member.controller;

import static com.seungah.todayclothes.global.jwt.JwtProvider.ACCESS_TOKEN_COOKIE_NAME;

import com.seungah.todayclothes.domain.member.dto.request.CheckAuthKeyRequest;
import com.seungah.todayclothes.domain.member.dto.request.SendAuthKeyRequest;
import com.seungah.todayclothes.domain.member.dto.request.SignInRequest;
import com.seungah.todayclothes.domain.member.dto.request.SignUpRequest;
import com.seungah.todayclothes.domain.member.dto.response.CheckAuthKeyResponse;
import com.seungah.todayclothes.domain.member.service.AuthService;
import com.seungah.todayclothes.global.jwt.TokenDto;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
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

	@PostMapping("/sign-in")
	public ResponseEntity<Void> signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response) {

		TokenDto dto = authService.signIn(request.getEmail(), request.getPassword());

		response.addHeader(HttpHeaders.SET_COOKIE, dto.getAccessTokenCookie().toString());
		response.addHeader(HttpHeaders.SET_COOKIE, dto.getRefreshTokenCookie().toString());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/sign-out")
	public ResponseEntity<Void> signOut(@AuthenticationPrincipal Long userId,
		@CookieValue(ACCESS_TOKEN_COOKIE_NAME) String accessToken
	) {
		authService.signOut(userId, accessToken);

		return ResponseEntity.ok().build();
	}

}
