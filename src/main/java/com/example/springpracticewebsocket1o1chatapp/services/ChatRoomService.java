package com.example.springpracticewebsocket1o1chatapp.services;

import java.util.Optional;

/**
 * Service interface for managing chat rooms.
 */
public interface ChatRoomService {

    /**
     * Gets the chat room ID for a given sender and recipient.
     *
     * @param senderId                 the sender's ID
     * @param recipientId              the recipient's ID
     * @param createNewRoomIfNotExists whether to create a new chat room if one doesn't exist
     * @return an optional containing the chat room ID if found or created, otherwise empty
     */
    Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    );

    /**
     * Creates a chat ID for a given sender and recipient.
     * @param senderId the sender's ID
     * @param recipientId the recipient's ID
     * @return the created chat ID
     */
    String createChatId(String senderId, String recipientId);

}
