package com.seungah.todayclothes.domain.clothes.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_BOTTOM;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_CHOICE;
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
		Member member = memberRepository.getReferenceById(userId);

		// schedule detail
		ScheduleDetail scheduleDetail = scheduleDetailRepository.findById(request.getScheduleDetailId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_SCHEDULE_DETAIL));

		// clothes
		Top top = topRepository.findById(request.getTopId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_TOP));
		Bottom bottom = bottomRepository.findById(request.getBottomId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_BOTTOM));

		// save
		clothesChoiceRepository.save(ClothesChoice.builder()
			.top(top)
			.bottom(bottom)
			.member(member)
			.scheduleDetail(scheduleDetail)
			.build());
	}

	@Transactional(readOnly = true)
	public Slice<ClothesChoiceResponse> getUserClothesChoiceList(
		Long userId, Long lastClothesChoiceId, Pageable pageable
	) {
		return clothesChoiceRepository
			.searchByMember(lastClothesChoiceId, userId, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<ClothesChoiceResponse> getOtherUserClothesChoiceList(
		Long userId, Long lastClothesChoiceId, Pageable pageable) {

		return clothesChoiceRepository
			.searchExceptForMember(lastClothesChoiceId, userId, pageable);
	}

	@Transactional
	public void deleteClothesChoice(Long userId, Long id) {

		Member member = memberRepository.getReferenceById(userId);

		ClothesChoice clothesChoice = clothesChoiceRepository.findById(id)
			.orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_CHOICE));

		clothesChoiceRepository.delete(clothesChoice);
	}
}
