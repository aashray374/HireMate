package com.squirtle.hiremate.user.service;

import com.squirtle.hiremate.auth.dto.SignUpRequest;
import com.squirtle.hiremate.auth.dto.VerifyOtp;
import com.squirtle.hiremate.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createNewUser(@Valid SignUpRequest request);
    void storeInRedisandSendOtp(@Valid SignUpRequest request);
    void verifyOtp(@Valid VerifyOtp request);
    User getUserByEmail(String email);
}
