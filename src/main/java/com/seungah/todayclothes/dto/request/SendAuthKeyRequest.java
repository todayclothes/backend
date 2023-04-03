package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SendAuthKeyRequest {

	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;

}
