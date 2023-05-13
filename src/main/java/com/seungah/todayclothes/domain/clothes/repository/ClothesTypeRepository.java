package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesType;
import com.seungah.todayclothes.global.type.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesTypeRepository extends JpaRepository<ClothesType, Long> {
    ClothesType findByGenderAndType(Gender gender, String type);
}
