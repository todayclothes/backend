package com.seungah.todayclothes.domain.member.entity;

import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.Gender;
import com.seungah.todayclothes.global.type.SignUpType;
import com.seungah.todayclothes.global.type.UserStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long kakaoUserId;
	private String naverUserId;

	@Column(length = 100, unique = true)
	private String email;
	private String name;
	private String password;

	@Enumerated(EnumType.STRING)
	private Gender gender;
	private String region; // TODO 지역 엔티티로 변경하기

	@Column(unique = true)
	private String phone;
	private boolean acceptMessage;

	@Enumerated(EnumType.STRING)
	private SignUpType signUpType;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	public Member kakaoIdUpdate(Long kakaoUserId) {
		this.kakaoUserId = kakaoUserId;
		return this;
	}
	public Member naverIdUpdate(String naverUserId) {
		this.naverUserId = naverUserId;
		return this;
	}

	public Member genderUpdate(String gender) {
		this.gender = Gender.valueOf(gender);
		if (this.userStatus == UserStatus.INACTIVE && this.isActive()) {
			this.userStatus = UserStatus.ACTIVE;
		}
		return this;
	}

	public Member regionUpdate(String region) {
		this.region = region;
		if (this.userStatus == UserStatus.INACTIVE && this.isActive()) {
			this.userStatus = UserStatus.ACTIVE;
		}
		return this;
	}

	public Member phoneUpdate(String phone) {
		this.phone = phone;
		return this;
	}

	public boolean isActive() {
		return this.gender != null && this.region != null;
	}

}