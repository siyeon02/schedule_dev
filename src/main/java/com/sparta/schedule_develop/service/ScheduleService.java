package com.sparta.schedule_develop.service;

import com.sparta.schedule_develop.ExceptionHandler.EntityNotFoundException;
import com.sparta.schedule_develop.ExceptionHandler.GlobalExceptionHandler;
import com.sparta.schedule_develop.dto.ScheduleRequestDto;
import com.sparta.schedule_develop.dto.ScheduleResponseDto;
import com.sparta.schedule_develop.entity.Dashboard;
import com.sparta.schedule_develop.entity.Schedule;
import com.sparta.schedule_develop.entity.User;
import com.sparta.schedule_develop.repository.DashboardRepository;
import com.sparta.schedule_develop.repository.ScheduleRepository;
import com.sparta.schedule_develop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final WeatherService weatherService;


    public Page<Schedule> getSchedule(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        return scheduleRepository.findAll(pageRequest);
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {

        User creator = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        String weather = weatherService.getTodayWeather();

        Schedule schedule = new Schedule(requestDto.getTitle(), requestDto.getContent(), weather, creator);
//        schedule.setCreator(creator);

        // DB에 스케줄 저장
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 담당 유저 할당 (assignedUserIds가 존재할 경우)
        if (requestDto.getAssignedUserIds() != null) {
            for (Long userId : requestDto.getAssignedUserIds()) {
                User assignedUser = userRepository.findById(userId)
                        .orElseThrow(() -> new GlobalExceptionHandler.InvalidTokenException("유효하지 않은 담당 유저입니다."));
                Dashboard dashboard = new Dashboard(savedSchedule, assignedUser);
                dashboardRepository.save(dashboard);
            }
        }

        // 스케줄을 저장 후, 응답 DTO로 변환
        return new ScheduleResponseDto(savedSchedule);

    }


    public ScheduleResponseDto getOneSchedule(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleResponseDto::new)
                .orElseThrow(() -> new EntityNotFoundException("선택한 스케줄은 존재하지 않습니다"));

    }

    @Transactional
    public Long updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);
        schedule.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public Long deleteSchedule(Long id) {
        Schedule schedule = findSchedule(id);
        scheduleRepository.delete(schedule);
        return id;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("선택한 스케줄은 존재하지 않습니다.")
        );
    }
}
