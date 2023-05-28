package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.TopQueryRepository;
import com.seungah.todayclothes.global.type.ClothesType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopRepository extends JpaRepository<Top, Long>, TopQueryRepository {
    Boolean existsByItemUrl(String itemUrl);

//    List<Top> findAllByClothesTypesInOrderByWeightDesc(List<ClothesType> clothesTypes, Plan plan);

//    @Query("SELECT t FROM Top t WHERE t.clothesType IN :clothesTypes ORDER BY t.weights DESC")
//    List<Top> findTopsByClothesTypesInOrderByWeightsDesc(@Param("clothesTypes") List<ClothesType> clothesTypes, Plan plan, Pageable pageable);
    List<Top> findAllByClothesTypeIn(List<ClothesType> clothesType);

    List<Top> findRandomEntitiesByClothesType(ClothesType clothesType, Pageable pageable);
}
