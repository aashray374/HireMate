package com.aashray.hiremate.exception;

public class IncorrectPassword extends RuntimeException {
    public IncorrectPassword() {
        super("Password is incorrect");
    }
}
