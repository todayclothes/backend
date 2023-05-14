package com.seungah.todayclothes.domain.member.dto.request;

import com.seungah.todayclothes.global.type.Gender;
import com.seungah.todayclothes.global.type.valid.EnumTypeValid;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateMemberInfoRequest {

	@NotNull(message = "지역을 입력해 주세요.")
	private Long regionId;

	@EnumTypeValid(enumClass = Gender.class, message = "Invalid Gender")
	@NotNull(message = "성별(MALE, FEMALE)를 선택하세요.")
	private String gender;

}
