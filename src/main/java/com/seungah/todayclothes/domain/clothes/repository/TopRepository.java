package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {
    Boolean existsByItemUrl(String itemUrl);

    @Query("SELECT e FROM Top e WHERE e.clothesGroup = :clothesGroup ORDER BY RAND()")
    List<Top> findRandomEntitiesByClothesGroup(@Param("clothesGroup") ClothesGroup clothesGroup, Pageable pageable);
}
