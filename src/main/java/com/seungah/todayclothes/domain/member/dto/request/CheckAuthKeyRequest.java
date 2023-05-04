package com.seungah.todayclothes.domain.member.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckAuthKeyRequest {

	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank(message = "인증 코드를 입력해 주세요.")
	private String authKey;
}
