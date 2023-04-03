package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRegionRequest {
	@NotNull(message = "지역을 입력해 주세요.")
	private String region;
}
