package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.BottomQueryRepository;
import com.seungah.todayclothes.global.type.ClothesType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BottomRepository extends JpaRepository<Bottom, Long>, BottomQueryRepository {
    Boolean existsByItemUrl(String itemUrl);

    List<Bottom> findRandomEntitiesByClothesType(ClothesType clothesType, Pageable pageable);
}
