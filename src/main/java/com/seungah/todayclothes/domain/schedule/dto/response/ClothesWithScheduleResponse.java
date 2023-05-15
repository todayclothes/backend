package com.seungah.todayclothes.domain.schedule.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClothesWithScheduleResponse {

	private ScheduleDto schedule;
	private ClothesDto clothesForNoSchedule;

	public ClothesWithScheduleResponse(ScheduleDto schedule) {
		this.schedule = schedule;
		this.clothesForNoSchedule = null;
	}

	public ClothesWithScheduleResponse(ClothesDto clothesDto) {
		this.schedule = null;
		this.clothesForNoSchedule = clothesDto;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ScheduleDto {

		private Long id;

		@JsonSerialize(using = LocalDateSerializer.class)
		@JsonDeserialize(using = LocalDateDeserializer.class)
		private LocalDate date;
		private List<ScheduleDetailDto> scheduleDetail;

		public static ScheduleDto of(Schedule schedule, List<ScheduleDetailDto> scheduleDetailList) {
			return ScheduleDto.builder()
				.id(schedule.getId())
				.date(schedule.getDate())
				.scheduleDetail(scheduleDetailList)
				.build();
		}

		@Getter
		@Builder
		@NoArgsConstructor
		@AllArgsConstructor
		public static class ScheduleDetailDto {

			private Long id;
			private String title;

			private TimeOfDay timeOfDay;
			private String plan;
			private String regionName;

			private ClothesDto clothes; // 해당 일정의 옷

			public static ScheduleDetailDto of(ScheduleDetail scheduleDetail, ClothesDto clothes) {
				return ScheduleDetailDto.builder()
					.id(scheduleDetail.getId())
					.title(scheduleDetail.getTitle())
					.timeOfDay(scheduleDetail.getTimeOfDay())
					.plan(scheduleDetail.getPlan())
					.regionName(scheduleDetail.getRegion().getName()) // TODO
					.clothes(clothes)
					.build();
			}

		}

	}
}
