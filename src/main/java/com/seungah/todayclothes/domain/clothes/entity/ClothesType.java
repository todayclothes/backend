package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.Gender;
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
public class ClothesType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String type;

    @OneToMany(mappedBy = "clothesType")
    private List<ClothesGroupType> clothesGroupTypes;
}