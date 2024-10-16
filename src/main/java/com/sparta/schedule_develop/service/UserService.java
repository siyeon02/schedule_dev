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
import jakarta.servlet.http.HttpServletResponse;
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
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");

        }

        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 다릅니다.");
            }
            role = UserRoleEnum.ADMIN;

        }

        User user = new User(username, password, email, role);
        userRepository.save(user);


    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);
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

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다"));
    }

    public Long deleteUser(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
        return id;
    }

}
