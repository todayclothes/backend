package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopLikeResponse {
    private Long id;

    private String imgUrl;
    private String itemUrl;

    private boolean isLiked;

    public static TopLikeResponse of(TopLike topLike){
        return TopLikeResponse.builder()
            .id(topLike.getId())
            .imgUrl(topLike.getTop().getImgUrl())
            .itemUrl(topLike.getTop().getItemUrl())
            .isLiked(true)
            .build();
    }
}
