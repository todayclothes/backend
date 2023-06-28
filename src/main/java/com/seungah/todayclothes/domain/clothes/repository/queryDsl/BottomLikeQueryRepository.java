package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.dto.response.BottomLikeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BottomLikeQueryRepository {

	Slice<BottomLikeResponse> searchByMember(
		Long lastBottomLikeId, Long userId, Pageable pageable
	);

}
