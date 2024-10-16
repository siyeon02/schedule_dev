package com.sparta.schedule_develop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleRequestDto {
    public Long userId;
    public String title;
    public String content;
    private List<Long> assignedUserIds;

}
