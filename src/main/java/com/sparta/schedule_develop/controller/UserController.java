package com.sparta.schedule_develop.controller;

import com.sparta.schedule_develop.dto.LoginRequestDto;
import com.sparta.schedule_develop.dto.SignupRequestDto;
import com.sparta.schedule_develop.dto.UserRequestDto;
import com.sparta.schedule_develop.dto.UserResponseDto;
import com.sparta.schedule_develop.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");

    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        userService.login(requestDto, res);
        return ResponseEntity.status(HttpStatus.CREATED).body("로그인 성공");
    }


//    @PostMapping("/users")
//    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
//        return userService.createUser(requestDto);
//    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/users/{id}")
    public Long updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/users/{id}")
    public Long deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
