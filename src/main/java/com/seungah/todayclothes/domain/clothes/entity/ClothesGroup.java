package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
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
public class ClothesGroup extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer groupNumber;

	@OneToMany(mappedBy = "clothesGroup")
	private List<ClothesGroupType> clothesGroupTypes;
}
