package com.example.springpracticewebsocket1o1chatapp.services;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findChatMessages(String senderId, String recipientId);

}
