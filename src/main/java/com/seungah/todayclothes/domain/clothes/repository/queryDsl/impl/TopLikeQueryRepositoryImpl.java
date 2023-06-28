package com.seungah.todayclothes.domain.clothes.repository.queryDsl.impl;

import static com.seungah.todayclothes.domain.clothes.entity.QTopLike.topLike;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.dto.response.TopLikeResponse;
import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.TopLikeQueryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TopLikeQueryRepositoryImpl implements TopLikeQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<TopLikeResponse> searchByMember(Long lastTopLikeId, Long userId,
		Pageable pageable) {
		List<TopLike> topLikeList = queryFactory
			.selectFrom(topLike)
			.leftJoin(topLike.member)
			.leftJoin(topLike.top).fetchJoin()
			.where(
				ltTopLikeId(lastTopLikeId),
				topLike.member.id.eq(userId)
			)
			.orderBy(topLike.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<TopLikeResponse> topLikeResponseList =
			topLikeList.stream()
				.map(TopLikeResponse::of)
				.collect(Collectors.toList());
		return checkLastPage(pageable, topLikeResponseList);

	}

	private BooleanExpression ltTopLikeId(Long topLikeId) {
		if (topLikeId == null) {
			return null;
		}

		return topLike.id.lt(topLikeId);
	}

	private Slice<TopLikeResponse> checkLastPage(Pageable pageable, List<TopLikeResponse> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

}
