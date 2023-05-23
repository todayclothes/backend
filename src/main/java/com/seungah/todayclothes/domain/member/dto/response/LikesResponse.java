package com.seungah.todayclothes.domain.member.dto.response;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.member.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesResponse {

	private Long id;
	private ClothesChoiceResponse clothes;

	public static LikesResponse of(Likes likes) {
		return LikesResponse.builder()
			.id(likes.getId())
			.clothes(ClothesChoiceResponse.of(likes.getClothesChoice()))
			.build();
	}

}
