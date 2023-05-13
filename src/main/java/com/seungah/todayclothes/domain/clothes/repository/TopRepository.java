package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {
    Boolean existsByItemUrl(String itemUrl);

    List<Top> findByClothesGroup(ClothesGroup clothesGroup);
}
