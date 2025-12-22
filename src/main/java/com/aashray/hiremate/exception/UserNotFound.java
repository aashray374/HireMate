package com.aashray.hiremate.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String email) {
        super("User with email : "+email+" does not exist");
    }
}