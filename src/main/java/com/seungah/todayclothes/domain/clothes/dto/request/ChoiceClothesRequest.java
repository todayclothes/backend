package com.seungah.todayclothes.domain.clothes.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChoiceClothesRequest {

	@NotNull(message = "상의 id는 null일 수 없습니다.")
	private Long topId;
	@NotNull(message = "하의 id는 null일 수 없습니다.")
	private Long bottomId;
	@NotNull(message = "상세 일정 id는 null일 수 없습니다.")
	private Long scheduleDetailId;

}
