package com.sparta.schedule_develop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    public String username;
    public String title;
    public String content;
    //private List<Long> assignedUserIds;

}
