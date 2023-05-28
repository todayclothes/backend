package com.seungah.todayclothes.global.ai.dto;

import com.seungah.todayclothes.global.type.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AiScheduleDto {
	private Plan plan;
	private String region;

	public static AiScheduleDto of(Plan plan, String region) {
		return AiScheduleDto.builder()
			.plan(plan)
			.region(region)
			.build();
	}
}
