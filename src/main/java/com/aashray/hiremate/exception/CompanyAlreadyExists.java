package com.aashray.hiremate.exception;

public class CompanyAlreadyExists extends RuntimeException {
    public CompanyAlreadyExists(String message) {
        super(message);
    }
}
