package com.aashray.hiremate.auth.controller;


import com.aashray.hiremate.auth.dto.AuthResponse;
import com.aashray.hiremate.auth.dto.CreateNewUserRequest;
import com.aashray.hiremate.auth.dto.LoginRequest;
import com.aashray.hiremate.security.jwt.JwtUtil;
import com.aashray.hiremate.user.dto.UserResponse;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.mapper.UserMapper;
import com.aashray.hiremate.user.repository.UserRepository;
import com.aashray.hiremate.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createNewUser(@RequestBody @Valid CreateNewUserRequest userRequest){
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userService.createNewUser(userMapper.toEntity(userRequest));
        return userMapper.createNewUserResponse(user);
    }


    @PostMapping("/login")
    public AuthResponse loginUser(@RequestBody @Valid LoginRequest loginRequest){
        User user = userService.loginUser(loginRequest.getEmail(),loginRequest.getPassword());
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }

}
