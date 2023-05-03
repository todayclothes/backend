package com.seungah.todayclothes.global.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		return ResponseEntity
			.status(e.getHttpStatus())
			.body(ErrorResponse.from(e.getErrorCode()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<String> params = new ArrayList<>();
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			params.add(error.getField() + ": " + error.getDefaultMessage());
		}
		String errorMessage = String.join(", ", params);

		ErrorResponse response = ErrorResponse.from(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
		response.changeMessage(errorMessage);

		return response;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	protected ErrorResponse handleRuntimeException(RuntimeException e) {
		log.error(e.getMessage());
		return ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
