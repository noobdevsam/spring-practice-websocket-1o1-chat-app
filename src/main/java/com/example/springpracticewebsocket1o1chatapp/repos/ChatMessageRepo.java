package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository for managing chat messages in the database.
 */
public interface ChatMessageRepo extends MongoRepository<ChatMessage, String> {

    /**
     * Finds all chat messages for a given chat ID.
     *
     * @param chatId the chat ID
     * @return a list of chat messages
     */
    List<ChatMessage> findByChatId(String chatId);

}
