package com.seungah.todayclothes.domain.member.repository.queryDsl;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.member.entity.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.seungah.todayclothes.domain.member.entity.QLikes.likes;

@Repository
@RequiredArgsConstructor
public class LikesQueryRepositoryImpl implements LikesQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ClothesChoiceResponse> searchByMember(
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

		List<ClothesChoiceResponse> clothesChoiceResponseList = likesList.stream()
			.map(likes -> ClothesChoiceResponse.of(likes.getClothesChoice(), userId)).collect(Collectors.toList());
		return checkLastPage(pageable, clothesChoiceResponseList);
	}

	private BooleanExpression ltLikesId(Long likesId) {
		if (likesId == null) {
			return null;
		}

		return likes.id.lt(likesId);
	}

	private Slice<ClothesChoiceResponse> checkLastPage(Pageable pageable, List<ClothesChoiceResponse> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}
}
