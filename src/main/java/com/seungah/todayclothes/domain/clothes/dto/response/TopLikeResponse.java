package com.seungah.todayclothes.domain.clothes.dto.response;

import com.seungah.todayclothes.domain.clothes.entity.Top;
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

    public static TopLikeResponse of(Long topLikeId, Top top){
        return TopLikeResponse.builder()
                .id(topLikeId)
                .imgUrl(top.getImgUrl())
                .itemUrl(top.getItemUrl())
                .isLiked(true)
                .build();
    }
}
