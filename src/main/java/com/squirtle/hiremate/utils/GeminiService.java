package com.squirtle.hiremate.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final ChatClient chatClient;
    public String getAnswer(String question) {
        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }
}