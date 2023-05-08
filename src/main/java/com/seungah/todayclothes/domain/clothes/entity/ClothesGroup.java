package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.type.ClothesName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClothesGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer groupNumber;

	@ElementCollection(targetClass = ClothesName.class)
	@Enumerated(EnumType.STRING)
	private List<ClothesName> clothesNames;

	public static ClothesGroup of(Integer groupNumber, List<ClothesName> clothesNames) {
		return ClothesGroup.builder()
				.groupNumber(groupNumber)
				.clothesNames(clothesNames)
				.build();
	}
}
