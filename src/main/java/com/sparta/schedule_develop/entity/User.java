package com.sparta.schedule_develop.entity;

import com.sparta.schedule_develop.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Dashboard> dashboardList = new ArrayList<>();

    public User(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
    }


    public void update(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
    }


}
