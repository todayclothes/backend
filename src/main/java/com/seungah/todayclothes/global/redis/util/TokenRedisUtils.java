package com.seungah.todayclothes.global.redis.util;

import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenRedisUtils {

	private final RedisTemplate<String, String> redisTemplate;

	public void put(Long userId, String refreshToken, Date expiredDate) {
		Date now = new Date();
		long expirationSeconds = (expiredDate.getTime() - now.getTime()) / 1000;

		if (expirationSeconds > 0) {
			ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
			valueOperations.set(String.valueOf(userId), refreshToken,
				Duration.ofSeconds(expirationSeconds));

		}
	}

	public boolean validateRefreshToken(Long userId, String refreshToken) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		String refreshTokenInRedis = values.get(String.valueOf(userId));

		if (refreshTokenInRedis == null) {
			return false;
		}

		return refreshTokenInRedis.equals(refreshToken);
	}

	public void delete(Long userId) {
		redisTemplate.delete(String.valueOf(userId));
	}

}
