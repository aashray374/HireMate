package com.aashray.hiremate.application.entity;

import java.util.EnumSet;
import java.util.Set;

public enum ApplicationStatus {

    APPLIED,
    OA,
    INTERVIEW,
    OFFER,
    ACCEPTED,
    REJECTED,
    GHOSTED,
    WITHDRAWN;

    private final Set<ApplicationStatus> allowedNext =
            EnumSet.noneOf(ApplicationStatus.class);

    static {
        APPLIED.allowedNext.addAll(EnumSet.of(
                OA, INTERVIEW, REJECTED, GHOSTED, WITHDRAWN
        ));

        OA.allowedNext.addAll(EnumSet.of(
                INTERVIEW, REJECTED, GHOSTED, WITHDRAWN
        ));

        INTERVIEW.allowedNext.addAll(EnumSet.of(
                OFFER, REJECTED, GHOSTED, WITHDRAWN
        ));

        OFFER.allowedNext.addAll(EnumSet.of(
                ACCEPTED, REJECTED, WITHDRAWN
        ));
    }

    public boolean canTransitionTo(ApplicationStatus next) {
        return next != null && allowedNext.contains(next);
    }
}
