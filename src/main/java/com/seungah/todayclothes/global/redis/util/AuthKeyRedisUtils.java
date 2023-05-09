package com.seungah.todayclothes.global.redis.util;

import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthKeyRedisUtils {

	private static final int VALID_TIME = 5;
	private final RedisTemplate<String, String> redisTemplate;

	public void put(String email, String authKey) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(email, authKey, Duration.ofMinutes(VALID_TIME));
	}

	public String get(String email) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String value = valueOperations.get(email);
		if (value == null) {
			throw new CustomException(ErrorCode.NOT_FOUND_AUTH_KEY);
		}
		return value;
	}

	public void delete(String email) {
		redisTemplate.delete(email);
	}

}
