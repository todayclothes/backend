package com.seungah.todayclothes.domain.region.service;

import com.seungah.todayclothes.domain.region.dto.response.GetRegionsResponse;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RegionService {

	private final RegionRepository regionRepository;

	@Transactional(readOnly = true)
	public List<GetRegionsResponse> getRegions() {
		List<Region> regionList = regionRepository.findAll();

		return regionList.stream().map(GetRegionsResponse::of)
			.collect(Collectors.toList());
	}

}
