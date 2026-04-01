package com.squirtle.hiremate.common.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EMAIL_QUEUE = "emailQueue";
    public static final String EMAIL_EXCHANGE = "emailExchange";
    public static final String EMAIL_ROUTING_KEY = "emailRoutingKey";

    public static final String EVENT_QUEUE = "eventQueue";
    public static final String EVENT_EXCHANGE = "eventExchange";
    public static final String EVENT_ROUTING_KEY = "eventRoutingKey";

    @Bean
    public Queue emaiQueue(){
        return new Queue(EMAIL_QUEUE,true);
    }

    @Bean
    public DirectExchange emailExchange(){
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder
                .bind(emaiQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Queue eventQueue(){
        return new Queue(EVENT_QUEUE,true);
    }

    @Bean
    public DirectExchange eventExchange(){
        return new DirectExchange(EVENT_EXCHANGE);
    }

    @Bean
    public Binding eventBinding(){
        return BindingBuilder
                .bind(eventQueue())
                .to(eventExchange())
                .with(EVENT_ROUTING_KEY);
    }
}
