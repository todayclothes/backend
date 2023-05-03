package com.seungah.todayclothes.repository.clothes;

import com.seungah.todayclothes.entity.clothes.ClothesChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesChoiceRepository extends JpaRepository<ClothesChoice, Long> {

}
