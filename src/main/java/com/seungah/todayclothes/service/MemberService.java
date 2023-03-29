package com.seungah.todayclothes.service;

import static com.seungah.todayclothes.common.exception.ErrorCode.NOT_FOUND_MEMBER;

import com.seungah.todayclothes.common.exception.CustomException;
import com.seungah.todayclothes.dto.response.GetProfileResponse;
import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.repository.MemberRepository;
import com.seungah.todayclothes.type.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public GetProfileResponse getProfile(Long userId) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member);
	}

	@Transactional
	public GetProfileResponse updateGender(Long userId, String gender) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member.genderUpdate(gender));
	}

	@Transactional
	public GetProfileResponse updateRegion(Long userId, String region) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

		return GetProfileResponse.of(member.regionUpdate(region));
	}
}
