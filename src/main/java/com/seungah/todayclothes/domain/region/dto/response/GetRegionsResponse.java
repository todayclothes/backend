package com.seungah.todayclothes.domain.region.dto.response;

import com.seungah.todayclothes.domain.region.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRegionsResponse {
	private Long regionId;
	private String regionName;

	public static GetRegionsResponse of(Region region) {
		return GetRegionsResponse.builder()
			.regionId(region.getId())
			.regionName(region.getName())
			.build();
	}
}
