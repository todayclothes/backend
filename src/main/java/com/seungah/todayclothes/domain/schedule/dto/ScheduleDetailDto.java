package com.seungah.todayclothes.domain.schedule.dto;

import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDto {
	private Long id;
	private String title;
	private String plan;
	private String regionName;

	public static ScheduleDetailDto of(ScheduleDetail scheduleDetail) {
		return ScheduleDetailDto.builder()
			.id(scheduleDetail.getId())
			.title(scheduleDetail.getTitle())
			.plan(scheduleDetail.getPlan())
			.regionName(scheduleDetail.getRegion().getName())
			.build();
	}

}
