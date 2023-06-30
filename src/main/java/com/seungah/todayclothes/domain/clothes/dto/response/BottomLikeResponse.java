package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.BottomLike;
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

    public static BottomLikeResponse of(BottomLike bottomLike){
        return BottomLikeResponse.builder()
            .id(bottomLike.getId())
            .imgUrl(bottomLike.getBottom().getImgUrl())
            .itemUrl(bottomLike.getBottom().getItemUrl())
            .isLiked(true)
            .build();
    }

}
