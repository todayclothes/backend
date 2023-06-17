package com.seungah.todayclothes.domain.clothes.service;

import com.seungah.todayclothes.domain.clothes.dto.response.BottomLikeResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopLikeResponse;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.BottomLike;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import com.seungah.todayclothes.domain.clothes.repository.BottomLikeRepository;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopLikeRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ClothesLikeService {
    private final TopLikeRepository topLikeRepository;
    private final BottomLikeRepository bottomLikeRepository;
    private final MemberRepository memberRepository;
    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;

    @Transactional
    public List<TopLikeResponse> getTopLike(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<TopLike> topLikes = topLikeRepository.findByMember(member);

        List<TopLikeResponse> topLikeResponses = new ArrayList<>();
        for (TopLike topLike : topLikes) {
            topLikeResponses.add(TopLikeResponse.of(topLike.getId(), topLike.getTop()));
        }
        return topLikeResponses;
    }
    @Transactional
    public List<BottomLikeResponse> getBottomLike(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<BottomLike> bottomLikes = bottomLikeRepository.findByMember(member);

        List<BottomLikeResponse> bottomLikeResponses = new ArrayList<>();

        for (BottomLike bottomLike : bottomLikes) {
            bottomLikeResponses.add(BottomLikeResponse.of(bottomLike.getId(), bottomLike.getBottom()));
        }
        return bottomLikeResponses;
    }

    @Transactional
    public void pressTopLike(Long userId, Long topId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Top top = topRepository.findById(topId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOP));

        boolean isDuplicate = topLikeRepository.existsByMemberAndTop(member, top);

        if (!isDuplicate){
            topLikeRepository.save(TopLike.builder()
                    .member(member)
                    .top(top)
                    .build());
        }
    }
    @Transactional
    public void pressBottomLike(Long userId, Long bottomId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Bottom bottom = bottomRepository.findById(bottomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOTTOM));


        boolean isDuplicate = bottomLikeRepository.existsByMemberAndBottom(member, bottom);

        if (!isDuplicate){
            bottomLikeRepository.save(BottomLike.builder()
                    .member(member)
                    .bottom(bottom)
                    .build());
        }
    }


    @Transactional
    public void cancelTopLike(Long userId, Long topId) {
        topLikeRepository.deleteByMemberAndTop(userId, topId);
    }
    @Transactional
    public void cancelBottomLike(Long userId, Long bottomId) {
        bottomLikeRepository.deleteByMemberAndBottom(userId, bottomId);
    }
}
