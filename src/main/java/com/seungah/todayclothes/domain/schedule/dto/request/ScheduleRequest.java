package com.seungah.todayclothes.domain.schedule.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class ScheduleRequest {
    private String title;
    private String year;
    private String month;
    private String day;
    private String location;
}
