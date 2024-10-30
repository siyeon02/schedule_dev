package com.sparta.schedule_develop.service;

import com.sparta.schedule_develop.config.JWTUtil;
import com.sparta.schedule_develop.config.PasswordEncoder;
import com.sparta.schedule_develop.dto.LoginRequestDto;
import com.sparta.schedule_develop.dto.SignupRequestDto;
import com.sparta.schedule_develop.dto.UserRequestDto;
import com.sparta.schedule_develop.dto.UserResponseDto;
import com.sparta.schedule_develop.entity.User;
import com.sparta.schedule_develop.entity.UserRoleEnum;
import com.sparta.schedule_develop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    // ADMIN_TOKEN
    @Value("${admin.token}")
    private final String adminToken;
//    ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, @Value("${admin.token}") String adminToken) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.adminToken = adminToken;
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(username, email);
        if (existingUser.isPresent()) {
            if (existingUser.get().getUsername().equals(username)) {
                throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
            } else if (existingUser.get().getEmail().equals(email)) {
                throw new IllegalArgumentException("중복된 이메일입니다.");
            }

        }

        UserRoleEnum role;
        if (requestDto.isAdmin()) {
            if (!adminToken.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 다릅니다.");
            }
            role = UserRoleEnum.ADMIN;

        } else {
            role = UserRoleEnum.USER;
        }


        User user = new User(username, password, email, role);
        userRepository.save(user);


    }

    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return jwtUtil.createToken(user.getUsername(), user.getRole());
    }

//    public UserResponseDto createUser(UserRequestDto requestDto) {
//        User user = new User(requestDto);
//        User saveUser = userRepository.save(user);
//        UserResponseDto userResponseDto = new UserResponseDto(saveUser);
//
//        return userResponseDto;
//    }

    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public Long updateUser(Long id, UserRequestDto requestDto) {
        User user = findUser(id);
        user.update(requestDto);
        return id;
    }

    public Long deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
        return id;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다"));
    }


}
