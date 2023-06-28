package com.seungah.todayclothes.domain.clothes.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_BOTTOM;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_TOP;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ClothesLikeService {
    private final TopLikeRepository topLikeRepository;
    private final BottomLikeRepository bottomLikeRepository;
    private final MemberRepository memberRepository;
    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;

    @Transactional
    public void pressTopLike(Long userId, Long topId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Top top = topRepository.findById(topId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TOP));

        boolean isDuplicate = topLikeRepository.existsByMemberAndTop(member, top);

        if (!isDuplicate){
            TopLike topLike = topLikeRepository.save(TopLike.builder()
                .member(member)
                .top(top)
                .build());
            top.getTopLikes().add(topLike);
        }
    }
    @Transactional
    public void pressBottomLike(Long userId, Long bottomId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));

        Bottom bottom = bottomRepository.findById(bottomId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOTTOM));


        boolean isDuplicate = bottomLikeRepository.existsByMemberAndBottom(member, bottom);

        if (!isDuplicate){
            BottomLike bottomLike = bottomLikeRepository.save(BottomLike.builder()
                .member(member)
                .bottom(bottom)
                .build());
            bottom.getBottomLikes().add(bottomLike);
        }
    }

    @Transactional(readOnly = true)
    public Slice<TopLikeResponse> getTopLike(
        Long userId, Long lastTopLikeId, Pageable pageable
    ) {
        if (!memberRepository.existsById(userId)) {
            throw new CustomException(NOT_FOUND_MEMBER);
        }

        return topLikeRepository
            .searchByMember(lastTopLikeId, userId, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<BottomLikeResponse> getBottomLike(
        Long userId, Long lastBottomLikeId, Pageable pageable
    ) {
        if (!memberRepository.existsById(userId)) {
            throw new CustomException(NOT_FOUND_MEMBER);
        }

        return bottomLikeRepository
            .searchByMember(lastBottomLikeId, userId, pageable);
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
