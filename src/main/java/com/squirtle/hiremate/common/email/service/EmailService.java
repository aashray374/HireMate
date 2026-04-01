package com.squirtle.hiremate.common.email.service;

import com.squirtle.hiremate.common.email.dto.ReferralEmailMessage;

public interface EmailService {
    void sendOtp(String to, String Otp);
    void sendReferralEmail(ReferralEmailMessage message);
}
