package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.dto.response.TopLikeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TopLikeQueryRepository {

	Slice<TopLikeResponse> searchByMember(
		Long lastTopLikeId, Long userId, Pageable pageable
	);

}
