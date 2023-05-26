package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.TopQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopRepository extends JpaRepository<Top, Long>, TopQueryRepository {
    Boolean existsByItemUrl(String itemUrl);

//    List<Top> findAllByClothesTypesInOrderByWeightDesc(List<ClothesType> clothesTypes, Plan plan);

//    @Query("SELECT t FROM Top t WHERE t.clothesType IN :clothesTypes ORDER BY t.weights DESC")
//    List<Top> findTopsByClothesTypesInOrderByWeightsDesc(@Param("clothesTypes") List<ClothesType> clothesTypes, Plan plan, Pageable pageable);
}
