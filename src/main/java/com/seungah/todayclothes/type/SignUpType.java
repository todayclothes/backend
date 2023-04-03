package com.seungah.todayclothes.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignUpType {
	EMAIL("todayclothes"),
	KAKAO("kakao"),
	NAVER("naver");

	private final String vendor;

}
