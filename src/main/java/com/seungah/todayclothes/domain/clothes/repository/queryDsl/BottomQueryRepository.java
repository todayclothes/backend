package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import java.util.List;
import java.util.Map;

public interface BottomQueryRepository {
	List<Bottom> findByClothesTypesAndWeightOrderByWeight(
		Map<ClothesType, Integer> memberClothesTypes,
		Plan plan);

}
