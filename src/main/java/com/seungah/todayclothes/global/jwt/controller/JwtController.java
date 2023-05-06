package com.seungah.todayclothes.global.jwt.controller;

import static com.seungah.todayclothes.global.jwt.JwtProvider.REFRESH_TOKEN_COOKIE_NAME;

import com.seungah.todayclothes.global.jwt.TokenDto;
import com.seungah.todayclothes.global.jwt.service.JwtService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tokens")
public class JwtController {
	private final JwtService jwtService;

	@PostMapping("/reissue")
	public ResponseEntity<Void> reissue(
		@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
		HttpServletResponse response
	) {

		TokenDto dto = jwtService.reissue(refreshToken);

		response.addHeader(HttpHeaders.SET_COOKIE, dto.getAccessTokenCookie().toString());
		response.addHeader(HttpHeaders.SET_COOKIE, dto.getRefreshTokenCookie().toString());

		return ResponseEntity.ok().build();

	}

}
