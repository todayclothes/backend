package com.seungah.todayclothes.entity;

import com.seungah.todayclothes.dto.request.ScheduleRequest;
import com.seungah.todayclothes.entity.common.BaseEntity;
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

    private LocalDate date; // TODO 우선 Integer year, month, day

    private String location; // TODO Region region 수정해야 할 것

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Schedule of(ScheduleRequest scheduleRequest, Member member) {
        return Schedule.builder()
                .title(scheduleRequest.getTitle())
                .date(scheduleRequest.getDate())
                .location(scheduleRequest.getLocation())
                .member(member)
                .build();
    }

    public void update(ScheduleRequest scheduleRequest) {
        this.title = scheduleRequest.getTitle();
        this.date= scheduleRequest.getDate();
        this.location = scheduleRequest.getLocation();
    }
}
