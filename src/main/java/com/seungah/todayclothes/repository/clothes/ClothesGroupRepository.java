package com.seungah.todayclothes.repository.clothes;

import com.seungah.todayclothes.entity.clothes.ClothesGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesGroupRepository extends JpaRepository<ClothesGroup, Long> {

}
