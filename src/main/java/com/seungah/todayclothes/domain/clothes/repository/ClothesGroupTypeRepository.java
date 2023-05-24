package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import com.seungah.todayclothes.domain.clothes.entity.ClothesGroupType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesGroupTypeRepository extends JpaRepository<ClothesGroupType, Long> {
    @Query("SELECT c FROM ClothesGroupType c WHERE c.clothesGroup = :clothesGroup ORDER BY RAND()")
    List<ClothesGroupType> findRandomEntitiesByClothesGroup(@Param("clothesGroup") ClothesGroup clothesGroup, Pageable pageable);
}
