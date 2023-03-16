package com.seungah.todayclothes.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	@JsonProperty("errorCode")
	private String code;
	@JsonProperty("errorMessage")
	private String message;

	public static ErrorResponse from(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}

	public void changeMessage(String message) {
		this.message = message;
	}
}
