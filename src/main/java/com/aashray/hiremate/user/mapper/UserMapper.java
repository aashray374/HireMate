package com.aashray.hiremate.user.mapper;

import com.aashray.hiremate.auth.dto.CreateNewUserRequest;
import com.aashray.hiremate.user.dto.UserResponse;
import com.aashray.hiremate.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateNewUserRequest userRequest){
        return User.builder()
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .build();
    }

    public UserResponse createNewUserResponse(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .cratedAt(user.getCreatedAt())
                .build();
    }

}
