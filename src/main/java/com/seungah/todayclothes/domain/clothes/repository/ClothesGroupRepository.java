package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesGroupRepository extends JpaRepository<ClothesGroup, Long> {
    ClothesGroup findByGroupNumber(Integer groupNumber);
}
