package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepo extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByChatId(String chatId);

}
