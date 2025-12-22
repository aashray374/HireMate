package com.aashray.hiremate.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String email) {
        super("The user with email : "+email+" already exists");
    }
}