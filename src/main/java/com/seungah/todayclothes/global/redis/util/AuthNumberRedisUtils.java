package com.seungah.todayclothes.global.redis.util;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_AUTH_NUMBER;

import com.seungah.todayclothes.global.exception.CustomException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthNumberRedisUtils {

	private static final int VALID_TIME = 5;
	private final RedisTemplate<String, String> redisTemplate;

	public void put(String phone, String authNumber) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(phone, authNumber, Duration.ofMinutes(VALID_TIME));
	}

	public String get(String phone) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String value = valueOperations.get(phone);
		if (value == null) {
			throw new CustomException(NOT_FOUND_AUTH_NUMBER);
		}
		return value;
	}

	public void delete(String phone) {
		redisTemplate.delete(phone);
	}

}
