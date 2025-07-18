package com.example.springpracticewebsocket1o1chatapp.services;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;

import java.util.List;

/**
 * Service interface for managing chat messages.
 */
public interface ChatMessageService {

    /**
     * Saves a chat message.
     *
     * @param chatMessage the chat message to save
     * @return the saved chat message
     */
    ChatMessage save(ChatMessage chatMessage);

    /**
     * Finds all chat messages between a sender and a recipient.
     * @param senderId the sender's ID
     * @param recipientId the recipient's ID
     * @return a list of chat messages
     */
    List<ChatMessage> findChatMessages(String senderId, String recipientId);

}
