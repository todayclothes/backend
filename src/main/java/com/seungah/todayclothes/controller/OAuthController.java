package com.seungah.todayclothes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seungah.todayclothes.dto.TokenDto;
import com.seungah.todayclothes.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class OAuthController {
    private final String naver = "naver";
    private final String kakao = "kakao";
    private final OAuthService oAuthService;

    @GetMapping("/")
    public ResponseEntity<Void> serverTest() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("kakao/callback")
    public ResponseEntity<Void> kakaoSignIn(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        TokenDto dto = oAuthService.socialSignIn(kakao, code, null, response);

        response.addHeader(HttpHeaders.SET_COOKIE, dto.getAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, dto.getRefreshTokenCookie().toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("naver/callback")
    public ResponseEntity<Void> naverLogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
        TokenDto dto = oAuthService.socialSignIn(naver, code, state, response);

        response.addHeader(HttpHeaders.SET_COOKIE, dto.getAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, dto.getRefreshTokenCookie().toString());

        return ResponseEntity.ok().build();
    }

}
