package com.aashray.hiremate.exception;

public class ResumeNotFound extends RuntimeException {
    public ResumeNotFound(Long id) {
        super("The Resume with id: "+id+" does not exists");
    }
}
