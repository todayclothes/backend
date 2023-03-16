package com.seungah.todayclothes.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* 400 */
	METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "METHOD_ARGUMENT_NOT_VALID", "입력값 유효성 검사에 실패하였습니다."),

	/* 500 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "예상치 못한 서버 에러가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
