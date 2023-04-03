package com.seungah.todayclothes.dto.request;

import com.seungah.todayclothes.type.Gender;
import com.seungah.todayclothes.type.valid.EnumTypeValid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateGenderRequest {

	@EnumTypeValid(enumClass = Gender.class, message = "Invalid Gender")
	@NotNull(message = "성별(MALE, FEMALE)를 선택하세요.")
	private String gender;

}
