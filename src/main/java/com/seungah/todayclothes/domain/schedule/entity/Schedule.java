package com.seungah.todayclothes.domain.schedule.entity;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.global.common.BaseEntity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints =
    @UniqueConstraint(columnNames = {"member_id", "date"})
)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Schedule createSchedule(Member member, LocalDate date) {
        Schedule schedule = new Schedule();
        schedule.setMember(member);
        schedule.setDate(date);
        return schedule;
    }

}
