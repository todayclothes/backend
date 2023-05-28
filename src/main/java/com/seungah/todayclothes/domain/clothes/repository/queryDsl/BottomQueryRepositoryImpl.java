package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import static com.seungah.todayclothes.domain.clothes.entity.QBottom.bottom;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BottomQueryRepositoryImpl implements BottomQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Bottom> findByClothesTypesAndWeightOrderByWeight(
		Map<ClothesType, Integer> memberClothesTypes,
		Plan plan
	) {

		JPAQuery<Bottom> query = queryFactory.selectFrom(bottom);

		// memberClothesTypes의 key에 해당하는 Top을 각각 value개씩 가져오기
		for (Map.Entry<ClothesType, Integer> entry : memberClothesTypes.entrySet()) {
			ClothesType clothesType = entry.getKey();
			Integer count = entry.getValue();

			query.where(bottom.clothesType.eq(clothesType))
				.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
				.limit(count);
		}

		// 랜덤 정렬
		query.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc());

		// Plan에 일치하는 bottom 높은 순서대로 가져오기
		query.where(bottom.planWeights.containsKey(plan))
			.orderBy(bottom.planWeights.get(plan).desc());

		// 3. 섞기
		return query.fetch();
	}

}
