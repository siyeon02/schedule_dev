package com.sparta.schedule_develop.entity;

import com.sparta.schedule_develop.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Comment(CommentRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.comment = requestDto.getComment();
    }

    public void update(CommentRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.comment = requestDto.getComment();
    }


}
