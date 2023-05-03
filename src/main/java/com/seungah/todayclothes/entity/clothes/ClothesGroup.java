package com.seungah.todayclothes.entity.clothes;

import com.seungah.todayclothes.type.ClothesName;
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
