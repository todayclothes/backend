package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import com.seungah.todayclothes.global.type.ClothesName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesGroupRepository extends JpaRepository<ClothesGroup, Long> {
    List<ClothesGroup> findByClothesNames(ClothesName clothesName);
}
