package com.seungah.todayclothes.domain.member.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_CHOICE;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.repository.ClothesChoiceRepository;
import com.seungah.todayclothes.domain.member.dto.response.LikesResponse;
import com.seungah.todayclothes.domain.member.entity.Likes;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.LikesRepository;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
			Likes like = likesRepository.save(
				Likes.builder()
					.clothesChoice(clothesChoice)
					.member(member)
					.build()
			);
			clothesChoice.getLikes().add(like);
		}

		updateWeights(member, clothesChoice);

	}

	@Transactional(readOnly = true)
	public Slice<LikesResponse> getUserLikeList(
		Long userId, Long lastLikesId, Pageable pageable
	) {

		return likesRepository.searchByMember(userId, lastLikesId, pageable);
	}

	@Transactional
	public void cancelLikeOnClothesChoice(Long userId, Long clothesChoiceId) {

		likesRepository.deleteByClothesChoiceIdAndMemberId(clothesChoiceId, userId);

	}

	private static void updateWeights(Member member, ClothesChoice clothesChoice){
		Plan plan = clothesChoice.getScheduleDetail().getPlan();

		clothesChoice.getTop().getPlanWeights()
			.put(plan, clothesChoice.getTop().getPlanWeights().get(plan) + 1);
		clothesChoice.getBottom().getPlanWeights()
			.put(plan, clothesChoice.getBottom().getPlanWeights().get(plan) + 1);

		member.getClothesTypeWeights().put(
			clothesChoice.getTop().getClothesType(),
			member.getClothesTypeWeights().get(clothesChoice.getTop().getClothesType()) + 1
		);

		member.getClothesTypeWeights().put(clothesChoice.getBottom().getClothesType(),
			member.getClothesTypeWeights().get(clothesChoice.getBottom().getClothesType()) + 1
		);
	}

}
