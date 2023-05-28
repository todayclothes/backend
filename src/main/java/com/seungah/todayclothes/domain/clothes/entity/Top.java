package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Top extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String imgUrl;
	private String itemUrl;

	private ClothesType clothesType;

	@ElementCollection
	@CollectionTable(name = "top_plan_weights", joinColumns = {@JoinColumn(name = "top_id")})
	@MapKeyColumn(name = "plan")
	@Column(name = "plan_weight")
	private Map<Plan, Integer> planWeights = new HashMap<>();

	public static Top of(String itemUrl, String imgUrl, ClothesType clothesType) {
		Map<Plan, Integer> planWeights = new HashMap<>();
		for (Plan plan : Plan.values()) {
			planWeights.put(plan, 1);
		}

		return Top.builder()
			.itemUrl(itemUrl)
			.imgUrl(imgUrl)
			.clothesType(clothesType)
			.planWeights(planWeights)
			.build();
	}
}
