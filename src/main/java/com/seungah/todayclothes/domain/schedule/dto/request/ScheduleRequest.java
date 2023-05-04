package com.seungah.todayclothes.domain.schedule.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class ScheduleRequest {
    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotBlank(message = "지역을 입력해 주세요.")
    private String location;
}
