package com.seungah.todayclothes.domain.schedule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import com.seungah.todayclothes.domain.schedule.dto.request.CreateScheduleRequest;
import com.seungah.todayclothes.domain.schedule.dto.response.ScheduleResponse;
import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ResponseEntity<ScheduleResponse> createSchedule(Long userId, CreateScheduleRequest createScheduleRequest) throws JsonProcessingException {

        // 1. Get Member.
        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        // 2. Plan 과 Region 을 받아 오기 위한 모델 요청
        String scheduleUrl = "http://52.79.45.112:5000/api/schedule";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(scheduleUrl)
                .queryParam("title", "승렬이와 뚝섬에서 런닝")
                .queryParam("region", "뚝섬");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String response = restTemplate.getForObject(builder.toUriString(), String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        String plan = rootNode.get("plan").asText();
        String regionName = rootNode.get("region").asText();

        // 3. 모델요청이 한번 더 있어야함.
        String clothesUrl = "http://52.79.45.112:5000/api/clothes";
        UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(clothesUrl)
                .queryParam("gender", member.getGender().getGenderType())
                .queryParam("humidity", "5")
                .queryParam("wind_speed", "3")
                .queryParam("rain", "0")
                .queryParam("temp", "14")
                .queryParam("schedule", plan);

        RestTemplate restTemplate2 = new RestTemplate();
        restTemplate2.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String response2 = restTemplate.getForObject(builder2.toUriString(), String.class);

        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode rootNode2 = objectMapper2.readTree(response2);
        String top = rootNode2.get("top").asText();
        String bottom = rootNode2.get("bottom").asText();

        // 4. Region 을 찾아서 옴
        Region region = regionRepository.findByName(regionName);

        // 5. Save Schedule.
        Schedule schedule = Schedule.of(createScheduleRequest, member, region);
        scheduleRepository.saveAndFlush(schedule);

        // 6. Return ScheduleResponse
        return ResponseEntity.ok(ScheduleResponse.of(schedule, plan, top, bottom));
    }


    // 2) Get Schedule List, 일주일치 데이터 값을 아예 생배로 주는 것이 이뻐보임.
    @Transactional
    public ResponseEntity<List<ScheduleResponse>> getSchedules(Long userId) {

        // 1. Get ScheduleList
        List<Schedule> scheduleList = scheduleRepository.findByMemberId(userId);

        // 2. Make ScheduleResponseList
        List<ScheduleResponse> scheduleResponsesList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            scheduleResponsesList.add(ScheduleResponse.of(schedule));
        }

        // 3. Return ScheduleResponseList
        return ResponseEntity.ok(scheduleResponsesList);
    }

    // 3) Update Schedule (Day)
    @Transactional
    public ResponseEntity<ScheduleResponse> updateSchedule(Long userId, CreateScheduleRequest createScheduleRequest, Long id) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        schedule.updateSchedule(createScheduleRequest);

        ScheduleResponse scheduleResponse = ScheduleResponse.of(schedule);

        return ResponseEntity.ok(scheduleResponse);
    }

    // 4) Delete Schedule
    @Transactional
    public ResponseEntity<Void> deleteSchedule(Long userId, Long id) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        scheduleRepository.delete(schedule);
        return ResponseEntity.ok().build();
    }

}
