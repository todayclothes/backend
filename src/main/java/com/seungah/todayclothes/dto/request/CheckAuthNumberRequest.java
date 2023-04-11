package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CheckAuthNumberRequest {

	@Pattern(regexp = "^(010)(\\d{3,4})(\\d{4})$", message = "010xxx(x)xxxx 형식에 맞게 입력하세요.")
	private String phone;

	@NotBlank(message = "인증 코드를 입력해 주세요.")
	private String authNumber;
}
