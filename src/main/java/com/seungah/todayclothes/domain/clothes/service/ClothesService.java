package com.seungah.todayclothes.domain.clothes.service;


import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

import com.seungah.todayclothes.domain.clothes.dto.response.ClothesDto.BottomDto;
import com.seungah.todayclothes.domain.clothes.dto.response.ClothesDto.TopDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;

    public ResponseEntity<List<TopDto>> getTopClothes(Integer groupNumber) {
        List<TopDto> topDtoList = new ArrayList<>();

        List<Top> topList = topRepository.findByClothesGroup(
            clothesGroupRepository.findByGroupNumber(groupNumber)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)));
        for (Top top : topList) {
            topDtoList.add(TopDto.of(top, groupNumber));
        }
        Collections.shuffle(topDtoList);
        return ResponseEntity.ok(topDtoList);
    }

    public ResponseEntity<List<BottomDto>> getBottomClothes(Integer groupNumber) {
        List<BottomDto> bottomDtoList = new ArrayList<>();

        List<Bottom> bottomList = bottomRepository.findByClothesGroup(
            clothesGroupRepository.findByGroupNumber(groupNumber)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)));
        for (Bottom bottom : bottomList) {
            bottomDtoList.add(BottomDto.of(bottom, groupNumber));
        }
        Collections.shuffle(bottomDtoList);
        return ResponseEntity.ok(bottomDtoList);
    }
}
