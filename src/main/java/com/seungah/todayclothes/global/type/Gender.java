package com.seungah.todayclothes.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
	MALE("0"),
	FEMALE("1");

	private final String GenderType;
}
