package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import static com.seungah.todayclothes.domain.clothes.entity.QTop.top;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TopQueryRepositoryImpl implements TopQueryRepository {
	private final JPAQueryFactory queryFactory;

	public List<Top> findByClothesTypesAndWeightOrderByWeight(
		Map<ClothesType, Integer> memberClothesTypes, // {니트: 1, 가디건: 6} 가중치
		Plan plan // 데이트, 한강
	) {

		JPAQuery<Top> query = queryFactory.selectFrom(top);

		// memberClothesTypes의 key에 해당하는 Top을 각각 value개씩 가져오기
		for (Map.Entry<ClothesType, Integer> entry : memberClothesTypes.entrySet()) {
			ClothesType clothesType = entry.getKey();
			Integer count = entry.getValue();

			query.where(top.clothesType.eq(clothesType))
				.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
				.limit(count);
		}

		// 랜덤 정렬
		query.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc());

		// Plan에 일치하는 Top 높은 순서대로 가져오기
		query.where(top.planWeights.containsKey(plan))
			.orderBy(top.planWeights.get(plan).desc());

		// 3. 섞기
		return query.fetch();
	}

	/*public List<Top> findByClothesTypesAndWeightOrderByWeight2(
		Map<ClothesType, Integer> memberClothesTypes, Plan plan
	) {

		// 1. plan이 높은 순서대로 정렬
		List<Top> result = queryFactory.selectFrom(top)
			.where(top.planWeights.containsKey(plan))
			.orderBy(top.planWeights.get(plan).desc())
			.fetch();

		// 2. memberClothesTypes의 key에 해당하는 Top을 각각 value개씩 가져오기
		List<Top> filteredResult = new ArrayList<>();
		for (Top topItem : result) {
			ClothesType clothesType = topItem.getClothesType();
			if (memberClothesTypes.containsKey(clothesType)) {
				int count = memberClothesTypes.get(clothesType);
				for (int i = 0; i < count; i++) {
					filteredResult.add(topItem);
				}
			}
		}

		// 3. 섞기
		Collections.shuffle(filteredResult);

		return filteredResult;
	}*/

}
