package com.sparta.schedule_develop.entity;

import com.sparta.schedule_develop.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "schedule")
@NoArgsConstructor
public class Schedule extends Timestamped {


    // 작성자와의 다대일 관계 (작성자 한 명이 여러 일정을 가질 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)  // 작성자 필드로 user 고유 식별자 사용
    private User creator;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "weather", length = 10000)
    private String weather;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(name = "username", nullable = false)
//    private String username;


    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    //다대다 관계
    @OneToMany(mappedBy = "schedule")
    private List<Dashboard> dashboardList = new ArrayList<>();


    public Schedule(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public Schedule(ScheduleRequestDto requestDto, String weather) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.weather = weather;
    }


    public void update(ScheduleRequestDto requestDto) {
        //this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }


}
