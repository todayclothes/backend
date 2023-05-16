package com.seungah.todayclothes.domain.clothes.entity;

import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.global.common.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "clothesChoiceWithAssociations",
	attributeNodes = {
		@NamedAttributeNode("top"),
		@NamedAttributeNode("bottom"),
		@NamedAttributeNode("scheduleDetail"),
		@NamedAttributeNode("member")
	}
)
public class ClothesChoice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "top_id")
	private Top top;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bottom_id")
	private Bottom bottom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_detail_id")
	private ScheduleDetail scheduleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

}
