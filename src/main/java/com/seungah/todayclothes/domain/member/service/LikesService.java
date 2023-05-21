package com.seungah.todayclothes.domain.member.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.*;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.repository.ClothesChoiceRepository;
import com.seungah.todayclothes.domain.member.entity.Likes;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.LikesRepository;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {
	private final MemberRepository memberRepository;
	private final LikesRepository likesRepository;
	private final ClothesChoiceRepository clothesChoiceRepository;

	@Transactional
	public void pressLikeOnClothesChoice(Long userId, Long clothesChoiceId) {
		Member member = memberRepository.getReferenceById(userId);

		ClothesChoice clothesChoice = clothesChoiceRepository.findById(clothesChoiceId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_CHOICE));

		if (!likesRepository.existsByClothesChoiceAndMember(clothesChoice, member)) {
			likesRepository.save(
				Likes.builder()
					.clothesChoice(clothesChoice)
					.member(member)
					.build()
			);
		}
	}

	@Transactional(readOnly = true)
	public List<ClothesChoiceResponse> getUserLikeList(Long userId) {
		Member member = memberRepository.getReferenceById(userId);

		List<ClothesChoice> clothesChoiceList =
			clothesChoiceRepository.findLikeClothesChoicesByMember(member);

		return clothesChoiceList.stream().map(ClothesChoiceResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void cancelLikeOnClothesChoice(Long userId, Long clothesChoiceId) {

		likesRepository.deleteByClothesChoiceIdAndMemberId(clothesChoiceId, userId);

	}
}
