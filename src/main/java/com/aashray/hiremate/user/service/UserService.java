package com.aashray.hiremate.user.service;

import com.aashray.hiremate.user.entity.User;
import jakarta.validation.constraints.Email;
import lombok.NonNull;

public interface UserService {
    User createNewUser(User entity);

    User loginUser(@NonNull @Email String email, @NonNull String password);

    User getUserFromEmail(@NonNull @Email String email);
}
