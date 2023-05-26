package com.seungah.todayclothes.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Plan {

	STUDY("공부"),
	DATE("데이트"),
	INTERVIEW("면접"),
	PART_TIME("알바"),
	TRIP("여행"),
	EXERCISE("운동"),
	HAN_RIVER("한강"),
	GRADUATION("졸업식"),
	SCHOOL("학교"),
	WORK("회사출근");

	private final String keyword;

}
