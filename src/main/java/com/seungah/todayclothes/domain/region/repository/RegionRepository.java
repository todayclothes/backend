package com.seungah.todayclothes.domain.region.repository;

import com.seungah.todayclothes.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByName(String name);

}
