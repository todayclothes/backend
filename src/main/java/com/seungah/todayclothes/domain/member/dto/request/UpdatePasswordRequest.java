package com.seungah.todayclothes.domain.member.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

	// TODO 패스워드 규칙
	@NotBlank(message = "패스워드를 입력하세요.")
	private String password;
}
