package com.seungah.todayclothes.domain.schedule.entity;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.schedule.dto.request.CreateScheduleRequest;
import com.seungah.todayclothes.global.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String plan;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Schedule of(CreateScheduleRequest createScheduleRequest, Member member, Region region) {
        return Schedule.builder()
                .title(createScheduleRequest.getTitle())
                .year(createScheduleRequest.getYear())
                .month(createScheduleRequest.getMonth())
                .day(createScheduleRequest.getDay())
                .member(member)
                .region(region)
                .build();
    }

    public static Schedule of(CreateScheduleRequest createScheduleRequest, Member member) {
        return Schedule.builder()
                .title(createScheduleRequest.getTitle())
                .year(createScheduleRequest.getYear())
                .month(createScheduleRequest.getMonth())
                .day(createScheduleRequest.getDay())
                .member(member)
                .build();
    }

    public void updateSchedule(CreateScheduleRequest createScheduleRequest) {
        this.title = createScheduleRequest.getTitle();
        this.year= createScheduleRequest.getYear();
        this.month= createScheduleRequest.getMonth();
        this.day= createScheduleRequest.getDay();
    }
}
