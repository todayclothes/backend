package com.seungah.todayclothes.repository.clothes;

import com.seungah.todayclothes.entity.clothes.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopRepository extends JpaRepository<Top, Long> {

}
