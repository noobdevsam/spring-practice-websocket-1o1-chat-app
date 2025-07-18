package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatRoom;
import com.example.springpracticewebsocket1o1chatapp.repos.ChatRoomRepo;
import com.example.springpracticewebsocket1o1chatapp.services.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;

    public ChatRoomServiceImpl(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    @Override
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepo
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    @Override
    public String createChatId(String senderId, String recipientId) {

        var chatId = String.format("%s_%s", senderId, recipientId);
        var senderRecipient = new ChatRoom(null, chatId, senderId, recipientId);
        var recipientSender = new ChatRoom(null, chatId, recipientId, senderId);

        chatRoomRepo.saveAll(
                List.of(senderRecipient, recipientSender)
        );

        return chatId;
    }
}
