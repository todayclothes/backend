package com.seungah.todayclothes.domain.clothes.dto;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.member.entity.Member;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        private boolean isLiked;

        public static TopDto of(Top top, Integer clothesGroupId) {
            return TopDto.builder()
                    .id(top.getId())
                    .itemUrl(top.getItemUrl())
                    .imgUrl(top.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .build();
        }
        public static TopDto of(Top top, Integer clothesGroupId, Member member) {
            boolean flag = top.getTopLikes().stream()
                .anyMatch(topLike -> topLike.getMember().equals(member));

            return TopDto.builder()
                    .id(top.getId())
                    .itemUrl(top.getItemUrl())
                    .imgUrl(top.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(flag)
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

        private boolean isLiked;

        public static BottomDto of(Bottom bottom, Integer clothesGroupId) {
            return BottomDto.builder()
                    .id(bottom.getId())
                    .itemUrl(bottom.getItemUrl())
                    .imgUrl(bottom.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .build();
        }
        public static BottomDto of(Bottom bottom, Integer clothesGroupId, Member member) {
            boolean flag = bottom.getBottomLikes().stream()
                .anyMatch(bottomLike -> bottomLike.getMember().equals(member));

            return BottomDto.builder()
                    .id(bottom.getId())
                    .itemUrl(bottom.getItemUrl())
                    .imgUrl(bottom.getImgUrl())
                    .clothesGroupId(clothesGroupId)
                    .isLiked(flag)
                    .build();
        }
    }
}