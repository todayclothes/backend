package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	private String password;

	@NotBlank(message = "이름을 입력해 주세요")
	private String name;

	@AssertTrue(message = "이메일 인증을 해주세요.")
	private boolean emailAuthResult;

}
