package com.sparta.schedule_develop.repository;

import com.sparta.schedule_develop.entity.Comment;
import com.sparta.schedule_develop.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySchedule(Schedule schedule);
}
