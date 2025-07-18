package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;
import com.example.springpracticewebsocket1o1chatapp.repos.ChatMessageRepo;
import com.example.springpracticewebsocket1o1chatapp.services.ChatMessageService;
import com.example.springpracticewebsocket1o1chatapp.services.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepo chatMessageRepo;
    private final ChatRoomService chatRoomService;

    public ChatMessageServiceImpl(ChatMessageRepo chatMessageRepo, ChatRoomService chatRoomService) {
        this.chatMessageRepo = chatMessageRepo;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {

        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElseThrow(
                () -> new RuntimeException("Chat room not found for senderId: " + chatMessage.getSenderId() +
                        " and recipientId: " + chatMessage.getRecipientId())
        );

        chatMessage.setChatId(chatId);

        chatMessageRepo.save(chatMessage);

        return chatMessage;
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {

        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);

        return chatId
                .map(chatMessageRepo::findByChatId)
                .orElse(
                        new ArrayList<>()
                );
    }
}
