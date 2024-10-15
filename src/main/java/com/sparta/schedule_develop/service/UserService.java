package com.sparta.schedule_develop.service;

import com.sparta.schedule_develop.dto.UserRequestDto;
import com.sparta.schedule_develop.dto.UserResponseDto;
import com.sparta.schedule_develop.entity.User;
import com.sparta.schedule_develop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = new User(requestDto);
        User saveUser = userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(saveUser);

        return userResponseDto;
    }

    public List<UserResponseDto> getUsers() {

        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public Long updateUser(Long id, UserRequestDto requestDto) {
        User user = findUser(id);
        user.update(requestDto);
        return id;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다"));
    }

    public Long deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
        return id;
    }
}
