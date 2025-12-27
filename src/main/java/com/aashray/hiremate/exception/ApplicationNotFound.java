package com.aashray.hiremate.exception;

public class ApplicationNotFound extends RuntimeException {
    public ApplicationNotFound() {
        super("Application Not Found");
    }
}
