package com.sparta.schedule_develop.service;

import com.sparta.schedule_develop.dto.ScheduleRequestDto;
import com.sparta.schedule_develop.dto.ScheduleResponseDto;
import com.sparta.schedule_develop.entity.Schedule;
import com.sparta.schedule_develop.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {
    public final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Page<Schedule> getSchedule(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        return scheduleRepository.findAll(pageRequest);
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);
        //db 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);
        return scheduleResponseDto;
    }

//    public List<ScheduleResponseDto> getSchedule() {
//        //db 조회
//        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
//    }

    public ScheduleResponseDto getOneSchedule(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("선택한 스케줄은 존재하지 않습니다"));

    }

    @Transactional
    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);
        schedule.update(requestDto);
        return id;
    }


    public Long deleteSchedule(Long id) {
        Schedule schedule = findSchedule(id);
        scheduleRepository.delete(schedule);
        return id;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 스케줄은 존재하지 않습니다.")
        );
    }
}
