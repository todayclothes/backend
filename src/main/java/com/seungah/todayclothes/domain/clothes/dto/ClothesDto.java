package com.seungah.todayclothes.domain.clothes.dto;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.BottomLike;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import com.seungah.todayclothes.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClothesDto {
    private List<TopDto> top;
    private List<BottomDto> bottom;

    public static ClothesDto of(List<TopDto> topList, List<BottomDto> bottomList) {
        return ClothesDto.builder()
                .top(topList)
                .bottom(bottomList)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopDto {
        private Long id;

        private String imgUrl;
        private String itemUrl;

        private Integer clothesGroupId;

        private Boolean isLiked;

        public static TopDto of(Top top, Integer clothesGroupId) {
            return TopDto.builder()
                    .id(top.getId())
                    .itemUrl(top.getItemUrl())
                    .imgUrl(top.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(false)
                    .build();
        }
        public static TopDto of(Top top, Integer clothesGroupId, Member member) {
            Long userId = top.getTopLikes().stream()
                    .filter(topLike -> topLike.getMember().equals(member))
                    .map(TopLike::getId)
                    .findFirst()
                    .orElse(-1L);

            return TopDto.builder()
                    .id(top.getId())
                    .itemUrl(top.getItemUrl())
                    .imgUrl(top.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(userId != -1)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BottomDto {
        private Long id;

        private String imgUrl;
        private String itemUrl;

        private Integer clothesGroupId;

        private Boolean isLiked;

        public static BottomDto of(Bottom bottom, Integer clothesGroupId) {
            return BottomDto.builder()
                    .id(bottom.getId())
                    .itemUrl(bottom.getItemUrl())
                    .imgUrl(bottom.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(false)
                    .build();
        }
        public static BottomDto of(Bottom bottom, Integer clothesGroupId, Member member) {
            Long userId = bottom.getBottomLikes().stream()
                    .filter(bottomLike -> bottomLike.getMember().equals(member))
                    .map(BottomLike::getId)
                    .findFirst()
                    .orElse(-1L);

            return BottomDto.builder()
                    .id(bottom.getId())
                    .itemUrl(bottom.getItemUrl())
                    .imgUrl(bottom.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(userId != -1)
                    .build();
        }
    }
}