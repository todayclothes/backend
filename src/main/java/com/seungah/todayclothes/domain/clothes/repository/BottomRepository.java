package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
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
public interface BottomRepository extends JpaRepository<Bottom, Long> {
    Boolean existsByItemUrl(String itemUrl);

    @Query("SELECT b FROM Bottom b JOIN FETCH b.planWeights WHERE b.id = :id")
    Optional<Bottom> findByIdWithPlanWeights(@Param("id") Long id);

    @Query("SELECT b FROM Bottom b "
        + "JOIN FETCH b.planWeights "
        + "WHERE b.clothesType = :clothesType "
        + "ORDER BY RAND()")
    @EntityGraph(attributePaths = "bottomLikes.member")
    List<Bottom> findRandomEntitiesByClothesType(
        @Param("clothesType") ClothesType clothesType, Pageable pageable);

}
