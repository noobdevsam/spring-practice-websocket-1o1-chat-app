package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository for managing chat rooms in the database.
 */
public interface ChatRoomRepo extends MongoRepository<ChatRoom, String> {

    /**
     * Finds a chat room by the sender and recipient IDs.
     *
     * @param senderId    the sender's ID
     * @param recipientId the recipient's ID
     * @return an optional containing the chat room if found, otherwise empty
     */
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

}
