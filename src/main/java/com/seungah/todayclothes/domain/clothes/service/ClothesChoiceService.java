package com.seungah.todayclothes.domain.clothes.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_BOTTOM;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_CHOICE;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_SCHEDULE_DETAIL;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_TOP;

import com.seungah.todayclothes.domain.clothes.dto.request.ChoiceClothesRequest;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesChoiceResponse;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesChoiceRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleDetailRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClothesChoiceService {

	private final ClothesChoiceRepository clothesChoiceRepository;
	private final ScheduleDetailRepository scheduleDetailRepository;
	private final MemberRepository memberRepository;
	private final TopRepository topRepository;
	private final BottomRepository bottomRepository;

	@Transactional
	public void choiceClothesOfSchedule(Long userId, ChoiceClothesRequest request) {
		// member
		Member member = memberRepository.findByIdWithClothesTypeWeights(userId);

		// schedule detail
		ScheduleDetail scheduleDetail = scheduleDetailRepository.findById(request.getScheduleDetailId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_SCHEDULE_DETAIL));

		// clothes
		Top top = topRepository.findByIdWithPlanWeights(request.getTopId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_TOP));
		Bottom bottom = bottomRepository.findByIdWithPlanWeights(request.getBottomId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_BOTTOM));

		// top, bottom 가중치랑 member 가중치 변화주기
		updateWeights(member, scheduleDetail, top, bottom);

		// save
		clothesChoiceRepository.save(
			ClothesChoice.of(top, bottom, member, scheduleDetail));

	}

	@Transactional(readOnly = true)
	public Slice<ClothesChoiceResponse> getUserClothesChoiceList(
		Long userId, Long lastClothesChoiceId, Pageable pageable
	) {
		return clothesChoiceRepository
			.searchByMember(lastClothesChoiceId, userId, pageable);
	}

	@Transactional
	public void deleteClothesChoice(Long userId, Long id) {
		if (!memberRepository.existsById(userId)) {
			throw new CustomException(NOT_FOUND_MEMBER);
		}

		ClothesChoice clothesChoice = clothesChoiceRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_CHOICE));

		clothesChoiceRepository.delete(clothesChoice);
	}

	private static void updateWeights(Member member, ScheduleDetail scheduleDetail, Top top, Bottom bottom) {
		top.getPlanWeights().put(
			scheduleDetail.getPlan(), top.getPlanWeights().get(scheduleDetail.getPlan()) + 1
		);
		bottom.getPlanWeights().put(
			scheduleDetail.getPlan(), bottom.getPlanWeights().get(scheduleDetail.getPlan()) + 1
		);

		member.getClothesTypeWeights().put(
			top.getClothesType(), member.getClothesTypeWeights().get(top.getClothesType()) + 1
		);
		member.getClothesTypeWeights().put(
			bottom.getClothesType(), member.getClothesTypeWeights().get(bottom.getClothesType()) + 1
		);
	}

}
