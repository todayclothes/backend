package com.seungah.todayclothes.entity;

import com.seungah.todayclothes.dto.request.ScheduleRequest;
import com.seungah.todayclothes.entity.common.BaseEntity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private LocalDate date;

    private String location; // 수정해야 할 것

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Schedule of(ScheduleRequest scheduleRequest, Member member) {
        return Schedule.builder()
                .title(scheduleRequest.getTitle())
                .date(scheduleRequest.getDate())
                .location(scheduleRequest.getLocation())
                .build();
    }

    public void update(ScheduleRequest scheduleRequest) {
        this.title = scheduleRequest.getTitle();
        this.date= scheduleRequest.getDate();
        this.location = scheduleRequest.getLocation();
    }
}
