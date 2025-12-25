package com.aashray.hiremate.exception;

public class IllegalOwnershipException extends RuntimeException {
    public IllegalOwnershipException() {
        super("Invalid company Id or resume Id");
    }
}
