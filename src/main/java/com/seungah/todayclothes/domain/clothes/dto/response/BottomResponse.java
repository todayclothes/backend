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
public class BottomResponse {
    private String itemUrl;
    private String imgUrl;

    public static BottomResponse of(Bottom bottom) {
        return BottomResponse.builder()
                .itemUrl(bottom.getItemUrl())
                .imgUrl(bottom.getImgUrl())
                .build();
    }
}
