package com.alpercan.freelance.frontend.service;

import com.alpercan.freelance.frontend.client.ChatServiceClient;
import com.alpercan.freelance.frontend.client.UserServiceClient; // EKLENDİ
import com.alpercan.freelance.frontend.dto.ConversationDTO;    // EKLENDİ
import com.alpercan.freelance.frontend.dto.MessageResponse;
import com.alpercan.freelance.frontend.dto.SendMessageRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class FrontendChatService {

    @Inject
    @RestClient
    ChatServiceClient chatClient;

    @Inject
    @RestClient
    UserServiceClient userClient; // EKLENDİ

    public void sendMessage(String token, Long receiverId, String content) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        SendMessageRequest request = new SendMessageRequest(receiverId, content);
        chatClient.sendMessage(token, request);
    }

    public List<MessageResponse> getConversation(String token, Long otherUserId) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        try {
            return chatClient.getConversation(token, otherUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // --- INBOX MANTIĞI ---
    public List<ConversationDTO> getInbox(String token) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;

        try {
            // 1. ID'leri al
            List<Long> userIds = chatClient.getConversationIds(token);

            // 2. İsimleri ve Fotoları al
            return userIds.stream().map(id -> {
                try {
                    // Public profili çek
                    var user = userClient.getUserById(id);
                    return new ConversationDTO(id, user.fullName(), user.pictureUrl());
                } catch (Exception e) {
                    return new ConversationDTO(id, "User #" + id, null);
                }
            }).toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}