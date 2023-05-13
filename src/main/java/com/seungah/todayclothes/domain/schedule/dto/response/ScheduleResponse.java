package com.seungah.todayclothes.domain.schedule.dto.response;

import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private String title;
    private String year;
    private String month;
    private String day;
    private String plan;
    private String top;
    private String bottom;
    public static ScheduleResponse of(Schedule schedule,String plan, String top, String bottom){
        return ScheduleResponse.builder()
                .title(schedule.getTitle())
                .year(schedule.getYear())
                .month(schedule.getMonth())
                .day(schedule.getDay())
                .plan(plan)
                .top(top)
                .bottom(bottom)
                .build();
    }
    public static ScheduleResponse of(Schedule schedule){
        return ScheduleResponse.builder()
                .title(schedule.getTitle())
                .year(schedule.getYear())
                .month(schedule.getMonth())
                .day(schedule.getDay())
                .build();
    }
}
