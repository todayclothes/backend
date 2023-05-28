package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.ClothesGroupType;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupTypeRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.type.ClothesType;
import com.seungah.todayclothes.global.type.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

@RequiredArgsConstructor
@Service
public class ClothesService {

    private final ClothesGroupRepository clothesGroupRepository;
    private final ClothesGroupTypeRepository clothesGroupTypeRepository;

	private final TopRepository topRepository;
	private final BottomRepository bottomRepository;


	public ClothesDto getClothesDto(Integer topGroupNumber, Integer bottomGroupNumber) {
		return ClothesDto.of(
				getTopClothes(topGroupNumber), getBottomClothes(bottomGroupNumber)
		);
	}

	public ClothesDto getRecommendClothesDto(
			Integer topGroupNumber, Integer bottomGroupNumber,
			Plan plan, Member member
	) {
		return ClothesDto.of(
				getRecommendTop(topGroupNumber, plan, member),
				getRecommendBottom(bottomGroupNumber, plan, member)
		);
	}

    private List<TopDto> getTopClothes(Integer groupNumber) {
        List<TopDto> topDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<ClothesGroupType> clothesGroupTypes = clothesGroupTypeRepository.findRandomEntitiesByClothesGroup(
                clothesGroupRepository.findByGroupNumber(groupNumber)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);

        for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
            topDtoList.add(TopDto.of(clothesGroupType.getTop(), groupNumber));
        }
        return topDtoList;
    }

    private List<BottomDto> getBottomClothes(Integer groupNumber) {
        List<BottomDto> bottomDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);

        List<ClothesGroupType> clothesGroupTypes = clothesGroupTypeRepository.findRandomEntitiesByClothesGroup(
                clothesGroupRepository.findByGroupNumber(groupNumber)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);

        for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
            bottomDtoList.add(BottomDto.of(clothesGroupType.getBottom(), groupNumber));
        }
        return bottomDtoList;
    }

	public List<TopDto> getRecommendTop(Integer groupNumber, Plan plan, Member member) {

		List<ClothesType> clothesTypes =
				clothesGroupRepository.findByGroupNumber(groupNumber).get().getClothesTypes();

		// 유저 weight 값 가져오기
		Map<ClothesType, Integer> memberClothesTypes = new HashMap<>();
		int totalWeight = 0;
		for (ClothesType key : clothesTypes) {
			memberClothesTypes.put(key, member.getClothesTypeWeights().get(key));
			totalWeight += member.getClothesTypeWeights().get(key);
		}

		List<Top> topList = new ArrayList<>();
		// 100개 - 퍼센트 계산
		for (Map.Entry<ClothesType, Integer> entry : memberClothesTypes.entrySet()){
			int percentage = (int) Math.round((entry.getValue() * 100.0) / totalWeight);
			memberClothesTypes.put(entry.getKey(), percentage);
			Pageable pageable = PageRequest.of(0, percentage);
			List<Top> findTopList = topRepository.findRandomEntitiesByClothesType(entry.getKey(), pageable);
			for (Top top : findTopList) {
				topList.add(top);
			}
		}

		Collections.shuffle(topList);

		topList.sort(Comparator.comparing(
				top -> top.getPlanWeights().getOrDefault(plan, 0), Comparator.reverseOrder()));

		return topList.stream().map(x -> TopDto.of(x, groupNumber))
				.collect(Collectors.toList());
	}
	public List<BottomDto> getRecommendBottom(Integer groupNumber, Plan plan, Member member) {

		// 타입 얻기
		List<ClothesType> clothesTypes =
				clothesGroupRepository.findByGroupNumber(groupNumber).get().getClothesTypes();


		// 유저 weight 값 가져오기
		Map<ClothesType, Integer> memberClothesTypes = new HashMap<>();
		int totalWeight = 0;
		for (ClothesType key : clothesTypes) {
			memberClothesTypes.put(key, member.getClothesTypeWeights().get(key));
			totalWeight += member.getClothesTypeWeights().get(key);
		}

		for (Map.Entry<ClothesType, Integer> entry : memberClothesTypes.entrySet()){
			int percentage = (int) Math.round((entry.getValue() * 100.0) / totalWeight);
			memberClothesTypes.put(entry.getKey(), percentage);
		}
		List<Bottom> bottomList = new ArrayList<>();
		// 100개 - 퍼센트 계산
		for (Map.Entry<ClothesType, Integer> entry : memberClothesTypes.entrySet()){
			int percentage = (int) Math.round((entry.getValue() * 100.0) / totalWeight);
			memberClothesTypes.put(entry.getKey(), percentage);
			Pageable pageable = PageRequest.of(0, percentage);
			List<Bottom> findBottomList = bottomRepository.findRandomEntitiesByClothesType(entry.getKey(), pageable);
			for (Bottom bottom : findBottomList) {
				bottomList.add(bottom);
			}
		}

		Collections.shuffle(bottomList);

		bottomList.sort(Comparator.comparing(
				bottom -> bottom.getPlanWeights().getOrDefault(plan, 0), Comparator.reverseOrder()));

		return bottomList.stream().map(x -> BottomDto.of(x, groupNumber))
				.collect(Collectors.toList());

	}
}
