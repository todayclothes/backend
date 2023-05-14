package com.seungah.todayclothes.global.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AiScheduleDto {
	private String plan;
	private String region;

	public static AiScheduleDto of(String plan, String region) {
		return AiScheduleDto.builder()
			.plan(plan)
			.region(region)
			.build();
	}
}
