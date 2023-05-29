package com.seungah.todayclothes.domain.member.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LikesQueryRepository {

	Slice<ClothesChoiceResponse> searchByMember(
		Long userId, Long lastLikesId, Pageable pageable
	);

}
