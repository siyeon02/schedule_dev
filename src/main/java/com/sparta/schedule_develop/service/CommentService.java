package com.sparta.schedule_develop.service;

import com.sparta.schedule_develop.dto.CommentRequestDto;
import com.sparta.schedule_develop.dto.CommentResponseDto;
import com.sparta.schedule_develop.entity.Comment;
import com.sparta.schedule_develop.entity.Schedule;
import com.sparta.schedule_develop.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleService scheduleService;

    // 생성자 주입 방식으로 의존성 주입
    public CommentService(CommentRepository commentRepository, ScheduleService scheduleService) {
        this.commentRepository = commentRepository;
        this.scheduleService = scheduleService;
    }

    public CommentResponseDto createComment(Schedule scheduleId, CommentRequestDto requestDto) {

        Comment comment = new Comment(requestDto);
        comment.setSchedule(scheduleId);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);

    }

    public List<CommentResponseDto> getAllComment(Schedule scheduleId) {
        return commentRepository.findBySchedule(scheduleId).stream().map(CommentResponseDto::new).toList();

    }

    public CommentResponseDto getComment(Schedule scheduleId, Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선택한 댓글이 존재하지 않습니다."));

        if (!comment.getSchedule().equals(scheduleId)) {
            throw new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다.");

        }

        return new CommentResponseDto(comment);
//        return commentRepository.findById(id)
//                .map(CommentResponseDto::new)
//                .orElseThrow(() -> new IllegalArgumentException("선택한 댓글이 존재하지 않습니다."));


    }

    @Transactional
    public Long updateComment(Schedule scheduleId, Long id, CommentRequestDto requestDto) {

        Comment comment = findById(id);
        comment.update(requestDto);

        return id;

    }


    public Long deleteComment(Schedule scheduleId, Long id) {
        Comment comment = findById(id);
        commentRepository.delete(comment);
        return id;
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
    }
}
