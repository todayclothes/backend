package com.seungah.todayclothes.domain.schedule.entity;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.schedule.dto.request.AddScheduleRequest;
import com.seungah.todayclothes.global.ai.dto.AiClothesDto;
import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.TimeOfDay;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints =
	@UniqueConstraint(columnNames = {"schedule_id", "time_of_day"})
)
public class ScheduleDetail extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;

	@Enumerated(EnumType.STRING)
	@Column(name = "time_of_day")
	private TimeOfDay timeOfDay;
	private String plan;

	private Integer topClothesGroup;
	private Integer bottomClothesGroup;

	private Double avgTemp;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@ManyToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	public static ScheduleDetail createScheduleDetail(AddScheduleRequest request,
		String plan, Region region, AiClothesDto aiClothesDto, Schedule schedule) {
		return ScheduleDetail.builder()
			.title(request.getTitle())
			.timeOfDay(TimeOfDay.valueOf(request.getTimeOfDay()))
			.plan(plan)
			.topClothesGroup(aiClothesDto.getTopGroup())
			.bottomClothesGroup(aiClothesDto.getBottomGroup())
			.avgTemp(aiClothesDto.getAvgTemp())
			.region(region)
			.schedule(schedule)
			.build();
	}

	public void updateScheduleDetail(AddScheduleRequest request,
		String plan, Region region, AiClothesDto aiClothesDto) {
		this.title = request.getTitle();
		this.timeOfDay = TimeOfDay.valueOf(request.getTimeOfDay());
		this.plan = plan;
		this.region = region;
		this.avgTemp = aiClothesDto.getAvgTemp();
		this.topClothesGroup = aiClothesDto.getTopGroup();
		this.bottomClothesGroup = aiClothesDto.getBottomGroup();

	}
}
