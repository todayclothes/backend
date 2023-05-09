package com.seungah.todayclothes.global.jwt.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.EXPIRED_REFRESH_TOKEN;

import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.jwt.JwtProvider;
import com.seungah.todayclothes.global.jwt.TokenDto;
import com.seungah.todayclothes.global.redis.util.TokenRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {
	private final JwtProvider jwtProvider;
	private final TokenRedisUtils tokenRedisUtils;

	// token 재발급
	@Transactional
	public TokenDto reissue(String refreshToken) {
		// Refresh Token 유효성 검증
		if (jwtProvider.isExpiredToken(refreshToken)) {
			throw new CustomException(EXPIRED_REFRESH_TOKEN);
		}

		// Refresh Token에서 User ID 추출
		Long userId = Long.parseLong(jwtProvider.getUserId(refreshToken));

		// Redis에서 Refresh Token 검증
		if (!tokenRedisUtils.validateRefreshToken(userId, refreshToken)) {
			throw new CustomException(EXPIRED_REFRESH_TOKEN);
		}

		// 새로운 Access Token 발급
		return jwtProvider.issueToken(userId);
	}
}
