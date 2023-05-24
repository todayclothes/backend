package com.seungah.todayclothes.domain.clothes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClothesGroupType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_group_id")
    private ClothesGroup clothesGroup;

    @ManyToOne
    @JoinColumn(name = "top_id")
    private Top top;

    @ManyToOne
    @JoinColumn(name = "bottom_id")
    private Bottom bottom;

    public static ClothesGroupType of(ClothesGroup clothesGroup, Top top, Bottom bottom) {
        return ClothesGroupType.builder()
                .clothesGroup(clothesGroup)
                .top(top)
                .bottom(bottom)
                .build();
    }
}