package com.seungah.todayclothes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

	private ResponseCookie accessTokenCookie;
	private ResponseCookie refreshTokenCookie;

}
