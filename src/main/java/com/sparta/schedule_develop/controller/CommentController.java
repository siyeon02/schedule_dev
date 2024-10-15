package com.sparta.schedule_develop.controller;

import com.sparta.schedule_develop.dto.CommentRequestDto;
import com.sparta.schedule_develop.dto.CommentResponseDto;
import com.sparta.schedule_develop.entity.Schedule;
import com.sparta.schedule_develop.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{scheduleId}")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public CommentResponseDto createComment(@PathVariable Schedule scheduleId, @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(scheduleId, requestDto);
    }

    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@PathVariable Schedule scheduleId) {
        return commentService.getAllComment(scheduleId);
    }

    @GetMapping("/comments/{id}")
    public CommentResponseDto getOneComments(@PathVariable Schedule scheduleId, @PathVariable Long id) {
        return commentService.getComment(scheduleId, id);
    }


}
