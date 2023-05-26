package com.seungah.todayclothes.domain.member.repository.queryDsl;


import static com.seungah.todayclothes.domain.member.entity.QLikes.likes;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.member.dto.response.LikesResponse;
import com.seungah.todayclothes.domain.member.entity.Likes;
import com.seungah.todayclothes.domain.member.repository.queryDsl.LikesQueryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikesQueryRepositoryImpl implements LikesQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<LikesResponse> searchByMember(
		Long userId, Long lastLikesId, Pageable pageable
	) {
		List<Likes> likesList = queryFactory
			.selectFrom(likes)
			.where(
				ltLikesId(lastLikesId),
				likes.member.id.eq(userId)
			)
			.orderBy(likes.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<LikesResponse> likesResponseList = likesList.stream()
			.map(LikesResponse::of).collect(Collectors.toList());
		return checkLastPage(pageable, likesResponseList);
	}

	private BooleanExpression ltLikesId(Long likesId) {
		if (likesId == null) {
			return null;
		}

		return likes.id.lt(likesId);
	}

	private Slice<LikesResponse> checkLastPage(Pageable pageable, List<LikesResponse> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}
}
