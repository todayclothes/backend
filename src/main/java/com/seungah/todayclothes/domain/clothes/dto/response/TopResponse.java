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
public class TopResponse {
    private String itemUrl;
    private String imgUrl;

    public static TopResponse of(Top top) {
        return TopResponse.builder()
                .itemUrl(top.getItemUrl())
                .imgUrl(top.getImgUrl())
                .build();
    }
}