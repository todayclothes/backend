package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.global.type.ClothesType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {
    Boolean existsByItemUrl(String itemUrl);

    @Query("SELECT t FROM Top t JOIN FETCH t.planWeights WHERE t.id = :id")
    Optional<Top> findByIdWithPlanWeights(@Param("id") Long id);

    @Query("SELECT t FROM Top t "
        + "JOIN FETCH t.planWeights "
        + "WHERE t.clothesType = :clothesType "
        + "ORDER BY RAND()")
    @EntityGraph(attributePaths = "topLikes.member")
    List<Top> findRandomEntitiesByClothesType(
        @Param("clothesType") ClothesType clothesType, Pageable pageable);

}