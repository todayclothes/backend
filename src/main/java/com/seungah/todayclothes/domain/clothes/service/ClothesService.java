package com.seungah.todayclothes.domain.clothes.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

import com.seungah.todayclothes.domain.clothes.dto.ClothesDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.ClothesDto.TopDto;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClothesService {

	private final TopRepository topRepository;
	private final BottomRepository bottomRepository;
	private final ClothesGroupRepository clothesGroupRepository;

	public ClothesDto getClothesDto(Integer topGroupNumber, Integer bottomGroupNumber) {
		return ClothesDto.of(
			getTopClothes(topGroupNumber), getBottomClothes(bottomGroupNumber)
		);
	}

	public List<TopDto> getTopClothes(Integer groupNumber) {
		List<TopDto> topDtoList = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 100);

		List<Top> topList = topRepository.findRandomEntitiesByClothesGroup(
			clothesGroupRepository.findByGroupNumber(groupNumber)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);
		for (Top top : topList) {
			topDtoList.add(TopDto.of(top, groupNumber));
		}
		Collections.shuffle(topDtoList);
		return topDtoList;
	}

	public List<BottomDto> getBottomClothes(Integer groupNumber) {
		List<BottomDto> bottomDtoList = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 100);

		List<Bottom> bottomList = bottomRepository.findRandomEntitiesByClothesGroup(
			clothesGroupRepository.findByGroupNumber(groupNumber)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)), pageable);
		for (Bottom bottom : bottomList) {
			bottomDtoList.add(BottomDto.of(bottom, groupNumber));
		}
		Collections.shuffle(bottomDtoList);
		return bottomDtoList;
	}

}
