package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothesGroupRepository extends JpaRepository<ClothesGroup, Long> {
    Optional<ClothesGroup> findByGroupNumber(Integer groupNumber);
}
