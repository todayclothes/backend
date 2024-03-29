package com.seungah.todayclothes.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/* 400 */
	METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "METHOD_ARGUMENT_NOT_VALID", "입력값 유효성 검사에 실패하였습니다."),
	ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "ALREADY_EXISTS_EMAIL", "이미 가입된 이메일 주소입니다."),
	WRONG_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "WRONG_EMAIL_OR_PASSWORD", "잘못된 이메일/비밀번호입니다."),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
	NOT_MATCH_SIGNUP_TYPE(HttpStatus.BAD_REQUEST, "NOT_MATCH_SIGNUP_TYPE", "vendor에 일치하는 회원가입 유형이 없습니다."),
	INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "INVALID_IMAGE_EXTENSION", "처리할 수 없는 이미지 형식입니다."),
	NOT_SEND_BOTH_GENDER_AND_REGION(HttpStatus.BAD_REQUEST, "NOT_SEND_BOTH_GENDER_AND_REGION", "성별, 지역 정보가 모두 필요합니다."),

	/* 401 */
	INVALID_AUTH(HttpStatus.UNAUTHORIZED, "INVALID_AUTH", "유효한 인증 정보가 아닙니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN", "Access Token이 만료되었습니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_REFRESH_TOKEN", "Refresh Token이 만료되었습니다."),

	/* 403 */
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),

	/* 404 */
	NOT_FOUND_AUTH_KEY(HttpStatus.NOT_FOUND, "NOT_FOUND_AUTH_KEY", "해당 인증 코드는 존재하지 않습니다."),
	NOT_FOUND_AUTH_NUMBER(HttpStatus.NOT_FOUND, "NOT_FOUND_AUTH_NUMBER", "해당 인증 번호는 존재하지 않습니다."),
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOT_FOUND_MEMBER", "해당 유저가 존재하지 않습니다."),
	NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "NOT_FOUND_EMAIL", "해당 이메일 주소가 존재하지 않습니다."),
	NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "NOT_FOUND_SCHEDULE", "스케쥴을 찾을 수 없습니다." ),
	NOT_FOUND_SCHEDULE_DETAIL(HttpStatus.NOT_FOUND, "NOT_FOUND_SCHEDULE_DETAIL", "상세 스케쥴을 찾을 수 없습니다." ),
	NOT_FOUND_REGION(HttpStatus.NOT_FOUND, "NOT_FOUND_REGION", "해당 지역이 없습니다."),
	NOT_FOUND_HOURLY_WEATHER(HttpStatus.NOT_FOUND, "NOT_FOUND_HOURLY_WEATHER", "실시간 날씨를 찾을 수 없습니다."),
	NOT_FOUND_DAILY_WEATHER(HttpStatus.NOT_FOUND, "NOT_FOUND_DAILY_WEATHER", "최저, 최고 기온을 찾을 수 없습니다."),
	NOT_FOUND_CLOTHES_GROUP(HttpStatus.NOT_FOUND, "NOT_FOUND_CLOTHES_GROUP", "의류 그룹을 찾을 수 없습니다."),
	NOT_FOUND_TOP(HttpStatus.NOT_FOUND, "NOT_FOUND_TOP", "해당 상의를 찾을 수 없습니다."),
	NOT_FOUND_BOTTOM(HttpStatus.NOT_FOUND, "NOT_FOUND_BOTTOM", "해당 하의를 찾을 수 없습니다."),
	NOT_FOUND_CLOTHES_CHOICE(HttpStatus.NOT_FOUND, "NOT_FOUND_CLOTHES_CHOICE", "해당 선택 의류를 찾을 수 없습니다."),

	/* 500 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "예상치 못한 서버 에러가 발생했습니다."),
	FAILED_SEND_MAIL(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_SEND_MAIL", "메일 전송에 실패했습니다."),
	FAILED_SEND_SMS(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_SEND_SMS", "SMS 전송에 실패했습니다."),
	FAILED_CALL_OPENWEATHERMAP_API(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_CALL_OPENWEATHERMAP_API", "OpenWeatherMap API 요청에 실패했습니다."),
	FAILED_CALL_AI_API(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_CALL_AI_API", "AI API 요청에 실패했습니다."),
	FAILED_FILE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_FILE_UPLOAD", "파일 업로드에 실패했습니다.")
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
