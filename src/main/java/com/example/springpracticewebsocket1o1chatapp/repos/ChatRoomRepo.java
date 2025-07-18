package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepo extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

}
