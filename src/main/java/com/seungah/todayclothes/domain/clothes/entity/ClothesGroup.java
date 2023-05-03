package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.type.ClothesName;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClothesGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer groupNumber;

	@ElementCollection(targetClass = ClothesName.class)
	@Enumerated(EnumType.STRING)
	private List<ClothesName> clothesNames;

}
