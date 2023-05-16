package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothesChoiceResponse {

	private Long memberId;

	private LocalDate date;
	private Double tempAvg;
	private String plan;

	private TopChoiceDto topChoice;
	private BottomChoiceDto bottomChoice;

	public static ClothesChoiceResponse of(ClothesChoice clothesChoice) {
		return ClothesChoiceResponse.builder()
			.memberId(clothesChoice.getMember().getId())
			.date(clothesChoice.getScheduleDetail().getSchedule().getDate())
			.tempAvg(clothesChoice.getScheduleDetail().getAvgTemp())
			.topChoice(TopChoiceDto.of(clothesChoice.getTop()))
			.bottomChoice(BottomChoiceDto.of(clothesChoice.getBottom()))
			.build();
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TopChoiceDto {

		private Long id;
		private String imgUrl;
		private String itemUrl;

		public static TopChoiceDto of(Top top) {
			return TopChoiceDto.builder()
				.id(top.getId())
				.imgUrl(top.getImgUrl())
				.itemUrl(top.getItemUrl())
				.build();
		}

	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BottomChoiceDto {

		private Long id;
		private String imgUrl;
		private String itemUrl;

		public static BottomChoiceDto of(Bottom bottom) {
			return BottomChoiceDto.builder()
				.id(bottom.getId())
				.imgUrl(bottom.getImgUrl())
				.itemUrl(bottom.getItemUrl())
				.build();
		}

	}

}
