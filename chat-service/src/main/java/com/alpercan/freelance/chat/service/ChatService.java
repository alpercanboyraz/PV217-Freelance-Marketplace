package com.alpercan.freelance.chat.service;

import com.alpercan.freelance.chat.dto.SendMessageRequest;
import com.alpercan.freelance.chat.model.Message;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ChatService {

    @Transactional
    public Message sendMessage(Long senderId, SendMessageRequest request) {
        Message message = new Message();
        message.senderId = senderId;
        message.receiverId = request.receiverId();
        message.content = request.content();

        message.persist();
        return message;
    }

    // İki kullanıcı arasındaki tüm mesajları getir (Ben sana attım VEYA sen bana attın)
    public List<Message> getConversation(Long userId1, Long userId2) {
        return Message.list(
                "(senderId = ?1 and receiverId = ?2) or (senderId = ?2 and receiverId = ?1) order by sentAt",
                userId1, userId2
        );
    }
}