package com.sparta.schedule_develop.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    public String comment;
    public Long schedule_id;
    private String username;

}
