package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BottomLikeResponse {
    private Long id;

    private String imgUrl;
    private String itemUrl;

    private boolean isLiked;

    public static BottomLikeResponse of(Long bottomLikeId, Bottom bottom){
        return BottomLikeResponse.builder()
                .id(bottomLikeId)
                .imgUrl(bottom.getImgUrl())
                .itemUrl(bottom.getItemUrl())
                .isLiked(true)
                .build();
    }
}
