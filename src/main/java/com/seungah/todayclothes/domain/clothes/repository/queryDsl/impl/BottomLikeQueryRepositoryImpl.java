package com.seungah.todayclothes.domain.clothes.repository.queryDsl.impl;

import static com.seungah.todayclothes.domain.clothes.entity.QBottomLike.bottomLike;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.dto.response.BottomLikeResponse;
import com.seungah.todayclothes.domain.clothes.entity.BottomLike;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.BottomLikeQueryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BottomLikeQueryRepositoryImpl implements BottomLikeQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<BottomLikeResponse> searchByMember(Long lastBottomLikeId, Long userId,
		Pageable pageable) {
		List<BottomLike> bottomLikeList = queryFactory
			.selectFrom(bottomLike)
			.leftJoin(bottomLike.member)
			.leftJoin(bottomLike.bottom).fetchJoin()
			.where(
				ltBottomLikeId(lastBottomLikeId),
				bottomLike.member.id.eq(userId)
			)
			.orderBy(bottomLike.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<BottomLikeResponse> bottomLikeResponseList =
			bottomLikeList.stream()
				.map(BottomLikeResponse::of)
				.collect(Collectors.toList());
		return checkLastPage(pageable, bottomLikeResponseList);

	}

	private BooleanExpression ltBottomLikeId(Long bottomLikeId) {
		if (bottomLikeId == null) {
			return null;
		}

		return bottomLike.id.lt(bottomLikeId);
	}

	private Slice<BottomLikeResponse> checkLastPage(Pageable pageable, List<BottomLikeResponse> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

}
