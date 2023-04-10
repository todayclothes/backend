package com.seungah.todayclothes.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleRequest {
    private String title;
    private LocalDate date;
    private String location;
}
