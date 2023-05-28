package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import static com.seungah.todayclothes.domain.clothes.entity.QClothesChoice.clothesChoice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class ClothesChoiceQueryRepositoryImpl implements ClothesChoiceQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ClothesChoiceResponse> searchByMember(
		Long lastClothesChoiceId, Long userId, Pageable pageable
	) {
		List<ClothesChoice> clothesChoiceList = queryFactory
			.selectFrom(clothesChoice)
			.where(
				ltClothesChoiceId(lastClothesChoiceId),
				clothesChoice.member.id.eq(userId)
			)
			.orderBy(clothesChoice.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ClothesChoiceResponse> clothesChoiceResponseList =
			clothesChoiceList.stream()
				.map(ClothesChoiceResponse::of)
				.collect(Collectors.toList());
		return checkLastPage(pageable, clothesChoiceResponseList);
	}

	@Override
	public Slice<ClothesChoiceResponse> searchExceptForMember(
		Long lastClothesChoiceId, Long userId, Pageable pageable
	) {
		List<ClothesChoice> clothesChoiceList = queryFactory
			.selectFrom(clothesChoice)
			.where(
				ltClothesChoiceId(lastClothesChoiceId),
				clothesChoice.member.id.ne(userId)
			)
			.orderBy(clothesChoice.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ClothesChoiceResponse> clothesChoiceResponseList =
			clothesChoiceList.stream()
				.map(clothesChoice -> ClothesChoiceResponse.of(clothesChoice, userId))
				.collect(Collectors.toList());
		return checkLastPage(pageable, clothesChoiceResponseList);
	}

	private BooleanExpression ltClothesChoiceId(Long clothesChoiceId) {
		if (clothesChoiceId == null) {
			return null;
		}

		return clothesChoice.id.lt(clothesChoiceId);
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
