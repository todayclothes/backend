package com.seungah.todayclothes.domain.clothes.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.schedule.dto.ScheduleDetailDto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesRecommendResponse {

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate date;

	private Period morning;
	private Period afternoon;
	private Period night;

	public ClothesRecommendResponse(LocalDate date) {
		this.date = date;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Period {
		private ScheduleDetailDto scheduleDetail;
		private ClothesDto clothes;

		public static Period of(ClothesDto clothes) {
			return Period.builder()
				.clothes(clothes)
				.build();
		}

		public static Period of(ScheduleDetailDto scheduleDetail, ClothesDto clothes) {
			return Period.builder()
				.scheduleDetail(scheduleDetail)
				.clothes(clothes)
				.build();
		}
	}

}
