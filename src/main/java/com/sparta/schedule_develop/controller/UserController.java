package com.sparta.schedule_develop.controller;

import com.sparta.schedule_develop.dto.UserRequestDto;
import com.sparta.schedule_develop.dto.UserResponseDto;
import com.sparta.schedule_develop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

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
