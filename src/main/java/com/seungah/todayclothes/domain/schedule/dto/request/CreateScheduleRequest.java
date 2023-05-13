package com.seungah.todayclothes.domain.schedule.dto.request;

import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    private String title;
    private String year;
    private String month;
    private String day;
}
