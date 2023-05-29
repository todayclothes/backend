package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.member.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothesChoiceResponse {
	private Long id;
	private Long memberId;

	private LocalDate date;
	private Double tempAvg;
	private String plan;

	private TopChoiceDto topChoice;
	private BottomChoiceDto bottomChoice;
	private Boolean isLiked;
	private Long likeId;

	public static ClothesChoiceResponse of(ClothesChoice clothesChoice) {
		return ClothesChoiceResponse.builder()
			.id(clothesChoice.getId())
			.memberId(clothesChoice.getMember().getId())
			.date(clothesChoice.getScheduleDetail().getSchedule().getDate())
			.tempAvg(clothesChoice.getScheduleDetail().getAvgTemp())
			.plan(clothesChoice.getScheduleDetail().getPlan().getKeyword())
			.topChoice(TopChoiceDto.of(clothesChoice.getTop()))
			.bottomChoice(BottomChoiceDto.of(clothesChoice.getBottom()))
			.likeId(-1L)
			.isLiked(false)
			.build();
	}

	public static ClothesChoiceResponse of(ClothesChoice clothesChoice, Long userId) {
		Long likeId = clothesChoice.getLikes().stream()
				.filter(likes -> likes.getMember().getId().equals(userId))
				.map(Likes::getId)
				.findFirst()
				.orElse(-1L);

		return ClothesChoiceResponse.builder()
				.id(clothesChoice.getId())
				.memberId(clothesChoice.getMember().getId())
				.date(clothesChoice.getScheduleDetail().getSchedule().getDate())
				.tempAvg(clothesChoice.getScheduleDetail().getAvgTemp())
				.plan(clothesChoice.getScheduleDetail().getPlan().getKeyword())
				.topChoice(TopChoiceDto.of(clothesChoice.getTop()))
				.bottomChoice(BottomChoiceDto.of(clothesChoice.getBottom()))
				.likeId(likeId)
				.isLiked(likeId != -1)
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
