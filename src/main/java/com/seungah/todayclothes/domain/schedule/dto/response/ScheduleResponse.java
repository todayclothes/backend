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
    private LocalDate date;
    private String location;

    public static ScheduleResponse of(Schedule schedule){
        return ScheduleResponse.builder()
                .title(schedule.getTitle())
                .date(schedule.getDate())
                .location(schedule.getLocation())
                .build();
    }
}
