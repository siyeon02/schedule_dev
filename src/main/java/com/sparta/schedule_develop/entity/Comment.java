package com.sparta.schedule_develop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "comment", nullable = false, length = 200)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(String comment, String username, Schedule schedule) {
        this.comment = comment;
        this.username = username;
        this.schedule = schedule;
    }
//    public Comment(CommentRequestDto requestDto) {
//        this.username = requestDto.getUsername();
//        this.comment = requestDto.getComment();
//    }

    public void update(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }


}
