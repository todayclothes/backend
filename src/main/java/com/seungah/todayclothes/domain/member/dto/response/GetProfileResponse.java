package com.seungah.todayclothes.domain.member.dto.response;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.global.type.Gender;
import com.seungah.todayclothes.global.type.SignUpType;
import com.seungah.todayclothes.global.type.UserStatus;
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
	private Gender gender;
	private String region;
	private String phone;
	private SignUpType signUpType;
	private UserStatus userStatus;

	public static GetProfileResponse of(Member member) {
		Region region = member.getRegion();
		return GetProfileResponse.builder()
			.memberId(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.gender(member.getGender())
			.region(region == null ? null : member.getRegion().getName())
			.phone(member.getPhone())
			.signUpType(member.getSignUpType())
			.userStatus(member.getUserStatus())
			.build();
	}

}
