package com.seungah.todayclothes.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SendAuthNumberRequest {
	@Pattern(regexp = "^(010)(\\d{3,4})(\\d{4})$", message = "010xxx(x)xxxx 형식에 맞게 입력하세요.")
	private String phone;

}
