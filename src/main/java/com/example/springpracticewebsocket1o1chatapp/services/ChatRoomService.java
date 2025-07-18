package com.example.springpracticewebsocket1o1chatapp.services;

import java.util.Optional;

public interface ChatRoomService {

    Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    );

    String createChatId(String senderId, String recipientId);

}
