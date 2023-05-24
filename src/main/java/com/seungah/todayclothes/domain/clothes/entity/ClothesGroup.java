package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.ClothesType;
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

	@ElementCollection
	@CollectionTable(name = "clothes_types", joinColumns = {@JoinColumn(name = "clothes_group_id")})
	@Enumerated(EnumType.STRING)
	@Column(name = "clothes_type")
	private List<ClothesType> clothesTypes;
}
