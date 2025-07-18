package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;
import com.example.springpracticewebsocket1o1chatapp.repos.ChatMessageRepo;
import com.example.springpracticewebsocket1o1chatapp.services.ChatMessageService;
import com.example.springpracticewebsocket1o1chatapp.services.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ChatMessageService interface.
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepo chatMessageRepo;
    private final ChatRoomService chatRoomService;

    /**
     * Constructs a new ChatMessageServiceImpl with the given repositories and services.
     *
     * @param chatMessageRepo the chat message repository
     * @param chatRoomService the chat room service
     */
    public ChatMessageServiceImpl(ChatMessageRepo chatMessageRepo, ChatRoomService chatRoomService) {
        this.chatMessageRepo = chatMessageRepo;
        this.chatRoomService = chatRoomService;
    }

    /**
     * Saves a chat message.
     * It finds or creates a chat room, sets the chat ID on the message, and saves it.
     * @param chatMessage the chat message to save
     * @return the saved chat message
     * @throws RuntimeException if a chat room cannot be found or created
     */
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

    /**
     * Finds all chat messages between a sender and a recipient.
     * It retrieves the chat room ID and then finds all messages for that chat ID.
     * @param senderId the sender's ID
     * @param recipientId the recipient's ID
     * @return a list of chat messages, or an empty list if no chat room is found
     */
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
