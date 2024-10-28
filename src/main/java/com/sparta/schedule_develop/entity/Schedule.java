package com.sparta.schedule_develop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "schedule")
@NoArgsConstructor
public class Schedule extends Timestamped {


    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comment = new ArrayList<>();
    //다대다 관계
    @OneToMany(mappedBy = "schedule")
    private final List<Dashboard> dashboardList = new ArrayList<>();
    // 작성자와의 다대일 관계 (작성자 한 명이 여러 일정을 가질 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)  // 작성자 필드로 user 고유 식별자 사용
    private User creator;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", length = 200)
    private String content;

    //    @Column(name = "username", nullable = false)
//    private String username;
    @Column(name = "weather")
    private String weather;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Schedule(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Schedule(String title, String content, String weather, User creator) {
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.creator = creator;
    }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
