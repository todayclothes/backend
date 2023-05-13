package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.response.BottomResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopResponse;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_CLOTHES_GROUP;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;

    public ResponseEntity<List<TopResponse>> getTopClothes(Integer groupNumber) {
        List<TopResponse> topResponseList = new ArrayList<>();

        List<Top> topList = topRepository.findByClothesGroup(clothesGroupRepository.findByGroupNumber(groupNumber)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)));
        for (Top top : topList) {
            topResponseList.add(TopResponse.of(top));
        }
        Collections.shuffle(topResponseList);
        return ResponseEntity.ok(topResponseList);
    }

    public ResponseEntity<List<BottomResponse>> getBottomClothes(Integer groupNumber) {
        List<BottomResponse> bottomResponseList = new ArrayList<>();

        List<Bottom> bottomList = bottomRepository.findByClothesGroup(clothesGroupRepository.
                findByGroupNumber(groupNumber).orElseThrow(() -> new CustomException(NOT_FOUND_CLOTHES_GROUP)));
        for (Bottom bottom : bottomList) {
            bottomResponseList.add(BottomResponse.of(bottom));
        }
        Collections.shuffle(bottomResponseList);
        return ResponseEntity.ok(bottomResponseList);
    }
}
