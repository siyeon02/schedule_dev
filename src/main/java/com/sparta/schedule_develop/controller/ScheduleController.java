package com.sparta.schedule_develop.controller;

import com.sparta.schedule_develop.dto.ScheduleRequestDto;
import com.sparta.schedule_develop.dto.ScheduleResponseDto;
import com.sparta.schedule_develop.entity.Schedule;
import com.sparta.schedule_develop.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;


    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedule")
    public Page<ScheduleResponseDto> getSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Schedule> schedules = scheduleService.getSchedule(page, size);
        return schedules.map(schedule -> new ScheduleResponseDto(
                schedule.getId(),
                schedule.getUsername(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                schedule.getComment().size() // 댓글 개수

        ));
    }

    @GetMapping("/schedule/{id}")
    public ScheduleResponseDto getOneSchedule(@PathVariable Long id) {
        return scheduleService.getOneSchedule(id);
    }

    @PutMapping("/schedule/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedule/{id}")
    public Long deleteSchedule(@PathVariable Long id) {
        return scheduleService.deleteSchedule(id);
    }
}
