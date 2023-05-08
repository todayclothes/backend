package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@ManyToOne
	@JoinColumn(name = "clothes_group_id")
	private ClothesGroup clothesGroup;

	public static Top of(String itemUrl, String imgUrl, ClothesGroup clothesGroup) {
		return Top.builder()
				.itemUrl(itemUrl)
				.imgUrl(imgUrl)
				.clothesGroup(clothesGroup)
				.build();
	}
}
