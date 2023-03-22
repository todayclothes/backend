package com.seungah.todayclothes.entity;

import com.seungah.todayclothes.entity.common.BaseEntity;
import com.seungah.todayclothes.type.Gender;
import com.seungah.todayclothes.type.SignUpType;
import com.seungah.todayclothes.type.UserStatus;
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
	private String NaverUserId;

	@Column(length = 100, unique = true)
	private String email;
	private String name;
	private String password;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(unique = true)
	private String kakaoUuid;
	private boolean acceptMessage;

	@Enumerated(EnumType.STRING)
	private SignUpType signUpType;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

}
