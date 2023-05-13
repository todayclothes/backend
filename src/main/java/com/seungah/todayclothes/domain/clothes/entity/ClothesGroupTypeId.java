package com.seungah.todayclothes.domain.clothes.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ClothesGroupTypeId implements Serializable {
    private Long clothesGroupId;
    private Long clothesTypeId;
}
