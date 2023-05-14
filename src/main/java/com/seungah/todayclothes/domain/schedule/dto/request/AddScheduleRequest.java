package com.seungah.todayclothes.domain.schedule.dto.request;

import com.seungah.todayclothes.global.type.TimeOfDay;
import com.seungah.todayclothes.global.type.valid.EnumTypeValid;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
public class AddScheduleRequest {

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate date;

	@NotBlank(message = "제목 입력은 필수입니다.")
	private String title;

	@EnumTypeValid(enumClass = TimeOfDay.class, message = "Invalid TimeOfDay")
	@NotNull(message = "시간대(MORNING, AFTERNOON, NIGHT)를 선택하세요.")
	private String timeOfDay;

	private String regionName;

}
