package com.aashray.hiremate.exception;

public class ResumeOfAnotherUser extends RuntimeException {
    public ResumeOfAnotherUser(Long id) {
        super("The Resume with id: " +id+"belongs to another user");
    }
}
