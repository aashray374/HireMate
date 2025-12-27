package com.aashray.hiremate.exception;

import com.aashray.hiremate.application.entity.ApplicationStatus;

public class IllegalStateTransition extends RuntimeException {
    public IllegalStateTransition(ApplicationStatus from,ApplicationStatus to) {
        super("Cannot go from "+ from.getClass().getName() +"to "+ to.getClass().getName()+ " directly");
    }
}
