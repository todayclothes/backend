package com.seungah.todayclothes.domain.member.repository;

import com.seungah.todayclothes.domain.member.dto.response.LikesResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LikesQueryRepository {

	Slice<LikesResponse> searchByMember(
		Long userId, Long lastLikesId, Pageable pageable
	);

}
