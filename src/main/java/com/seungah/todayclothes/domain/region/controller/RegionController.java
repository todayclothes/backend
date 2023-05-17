package com.seungah.todayclothes.domain.region.controller;

import com.seungah.todayclothes.domain.region.dto.response.GetRegionsResponse;
import com.seungah.todayclothes.domain.region.service.RegionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/regions")
public class RegionController {

	private final RegionService regionService;

	@GetMapping
	public ResponseEntity<List<GetRegionsResponse>> getRegions() {

		return ResponseEntity.ok(regionService.getRegions());
	}
}
