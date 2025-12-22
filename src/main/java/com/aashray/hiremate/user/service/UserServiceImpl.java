package com.aashray.hiremate.user.service;

import com.aashray.hiremate.exception.IncorrectPassword;
import com.aashray.hiremate.exception.UserAlreadyExists;
import com.aashray.hiremate.exception.UserNotFound;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createNewUser(User entity) {
        if(userRepository.findByEmail(entity.getEmail()).isPresent()){
            throw new UserAlreadyExists(entity.getEmail());
        }
        return userRepository.save(entity);
    }

    @Override
    public User loginUser(@NonNull String email, @NonNull String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new UserNotFound(email);
        }
        if(passwordEncoder.matches(password,user.getPassword())){
            return user;
        }
        throw new IncorrectPassword();
    }
}
