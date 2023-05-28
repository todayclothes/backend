package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.global.type.ClothesType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {
    Boolean existsByItemUrl(String itemUrl);

    List<Top> findRandomEntitiesByClothesType(ClothesType clothesType, Pageable pageable);
}
