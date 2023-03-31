package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateNameRequest {

	@NotBlank(message = "이름을 입력해 주세요.")
	private String name;

}
