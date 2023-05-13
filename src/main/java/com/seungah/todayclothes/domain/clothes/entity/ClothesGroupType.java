package com.seungah.todayclothes.domain.clothes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClothesGroupType {
    @EmbeddedId
    private ClothesGroupTypeId id;

    @ManyToOne
    @MapsId("clothesGroupId")
    private ClothesGroup clothesGroup;

    @ManyToOne
    @MapsId("clothesTypeId")
    private ClothesType clothesType;
}