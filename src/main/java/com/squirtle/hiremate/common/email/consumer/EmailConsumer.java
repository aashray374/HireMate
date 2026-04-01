package com.squirtle.hiremate.common.email.consumer;


import com.squirtle.hiremate.common.config.RabbitMQConfig;
import com.squirtle.hiremate.common.email.dto.ReferralEmailMessage;
import com.squirtle.hiremate.common.email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consume(ReferralEmailMessage message){
        emailService.sendReferralEmail(message);
    }
}
