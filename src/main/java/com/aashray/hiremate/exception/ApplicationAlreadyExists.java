package com.aashray.hiremate.exception;

public class ApplicationAlreadyExists extends RuntimeException {
    public ApplicationAlreadyExists() {
        super("The Application Already exists");
    }
}
