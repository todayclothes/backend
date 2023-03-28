package com.seungah.todayclothes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignInRequest {
    private String id;
    private String nickname;
    private String userEmail;

}
