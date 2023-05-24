package com.seungah.todayclothes.global.jwt;

public class TokenExpirationConfig {

	// TODO 30 * 60 * 1000L; // 30분
	private static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 하루
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

	public static long getAccessTokenExpirationTime() {
		return ACCESS_TOKEN_EXPIRATION_TIME;
	}

	public static long getRefreshTokenExpirationTime() {
		return REFRESH_TOKEN_EXPIRATION_TIME;
	}
}
