package com.seungah.todayclothes.domain.clothes.repository.queryDsl;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import java.util.List;
import java.util.Map;

public interface TopQueryRepository {

	List<Top> findByClothesTypesAndWeightOrderByWeight(
		Map<ClothesType, Integer> memberClothesTypes,
		Plan plan);

}
