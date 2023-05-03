package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.global.common.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClothesChoice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "top_id")
	private Top top;

	@OneToOne
	@JoinColumn(name = "bottom_id")
	private Bottom bottom;

	@OneToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

}
