package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ClothesChoiceQueryRepository {

	Slice<ClothesChoiceResponse> searchByMember(
		Long lastClothesChoiceId, Long userId, Pageable pageable
	);

	Slice<ClothesChoiceResponse> searchExceptForMember(
		Long lastClothesChoiceId, Long userId, Pageable pageable
	);
}