package com.seungah.todayclothes.domain.member.entity;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_SEND_BOTH_GENDER_AND_REGION;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Gender;
import com.seungah.todayclothes.global.type.SignUpType;
import com.seungah.todayclothes.global.type.UserStatus;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
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

	@Column(unique = true)
	private Long kakaoUserId;
	@Column(unique = true)
	private String naverUserId;

	@Column(length = 100, unique = true)
	private String email;
	private String name;
	private String password;

	@Enumerated(EnumType.STRING)
	private Gender gender;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	private Region region;

	@Column(unique = true)
	private String phone;

	// TODO
	@ElementCollection
	@CollectionTable(name = "clothes_type_weights", joinColumns = {@JoinColumn(name = "member_id")})
	@MapKeyColumn(name = "clothes_type")
	@Column(name = "clothes_type_weight")
	private Map<ClothesType, Integer> clothesTypeWeights = new HashMap<>();

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

	public void activeMemberInfoUpdate(Region region, String gender) {
		if (gender == null || region == null) {
			throw new CustomException(NOT_SEND_BOTH_GENDER_AND_REGION);
		}

		this.gender = Gender.valueOf(gender);
		this.region = region;
		this.userStatus = UserStatus.ACTIVE;
	}
}
