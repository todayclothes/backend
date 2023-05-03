package com.seungah.todayclothes.entity.clothes;

import com.seungah.todayclothes.entity.Member;
import com.seungah.todayclothes.entity.Schedule;
import com.seungah.todayclothes.entity.common.BaseEntity;
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
