package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class ClothesDto {
	private List<TopDto> top = new ArrayList<>();
	private List<BottomDto> bottom = new ArrayList<>();

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TopDto {
		private Long id;

		private String imgUrl;
		private String itemUrl;

		private Integer clothesGroupId;

		public static TopDto of(Top top, Integer clothesGroupId) {
			return TopDto.builder()
				.id(top.getId())
				.itemUrl(top.getItemUrl())
				.imgUrl(top.getImgUrl())
				.clothesGroupId(clothesGroupId)
				.build();
		}
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BottomDto {
		private Long id;

		private String imgUrl;
		private String itemUrl;

		private Integer clothesGroupId;

		public static BottomDto of(Bottom bottom, Integer clothesGroupId) {
			return BottomDto.builder()
				.id(bottom.getId())
				.itemUrl(bottom.getItemUrl())
				.imgUrl(bottom.getImgUrl())
				.clothesGroupId(clothesGroupId)
				.build();
		}
	}
}
