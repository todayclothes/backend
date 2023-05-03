package com.seungah.todayclothes.repository.clothes;

import com.seungah.todayclothes.entity.clothes.Bottom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BottomRepository extends JpaRepository<Bottom, Long> {

}
