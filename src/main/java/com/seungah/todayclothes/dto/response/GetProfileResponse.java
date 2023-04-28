package com.seungah.todayclothes.dto.response;

import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.type.Gender;
import com.seungah.todayclothes.type.SignUpType;
import com.seungah.todayclothes.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProfileResponse {
	private Long memberId;
	private String email;
	private String name;
	//private String password;
	private Gender gender;
	private String region;
	private String phone;
	private SignUpType signUpType;
	private UserStatus userStatus;

	public static GetProfileResponse of(Member member) {
		return GetProfileResponse.builder()
			.memberId(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.gender(member.getGender())
			.region(member.getRegion())
			.phone(member.getPhone())
			.signUpType(member.getSignUpType())
			.userStatus(member.getUserStatus())
			.build();
	}

}
