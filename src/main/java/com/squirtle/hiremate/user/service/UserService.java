package com.squirtle.hiremate.user.service;

import com.squirtle.hiremate.auth.dto.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createNewUser(@Valid SignUpRequest request);
}
