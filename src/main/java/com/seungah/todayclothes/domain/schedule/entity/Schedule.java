package com.seungah.todayclothes.domain.schedule.entity;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.schedule.dto.request.ScheduleRequest;
import com.seungah.todayclothes.global.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String year;
    private String month;
    private String day;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Schedule of(ScheduleRequest scheduleRequest, Member member, Region region) {
        return Schedule.builder()
                .title(scheduleRequest.getTitle())
                .year(scheduleRequest.getYear())
                .month(scheduleRequest.getMonth())
                .day(scheduleRequest.getDay())
                .member(member)
                .region(region)
                .build();
    }

    public static Schedule of(ScheduleRequest scheduleRequest, Member member) {
        return Schedule.builder()
                .title(scheduleRequest.getTitle())
                .year(scheduleRequest.getYear())
                .month(scheduleRequest.getMonth())
                .day(scheduleRequest.getDay())
                .member(member)
                .build();
    }

    public void updateSchedule(ScheduleRequest scheduleRequest) {
        this.title = scheduleRequest.getTitle();
        this.year= scheduleRequest.getYear();
        this.month= scheduleRequest.getMonth();
        this.day= scheduleRequest.getDay();
    }
}
