package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatRoom;
import com.example.springpracticewebsocket1o1chatapp.repos.ChatRoomRepo;
import com.example.springpracticewebsocket1o1chatapp.services.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ChatRoomService interface.
 */
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;

    /**
     * Constructs a new ChatRoomServiceImpl with the given chat room repository.
     *
     * @param chatRoomRepo the chat room repository
     */
    public ChatRoomServiceImpl(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    /**
     * Gets the chat room ID for a given sender and recipient.
     * It first searches for an existing chat room. If not found, it may create a new one.
     * @param senderId the sender's ID
     * @param recipientId the recipient's ID
     * @param createNewRoomIfNotExists whether to create a new chat room if one doesn't exist
     * @return an optional containing the chat room ID if found or created, otherwise empty
     */
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

    /**
     * Creates a new chat ID and corresponding chat room entries for a sender and recipient.
     * It creates two entries, one for each direction of communication.
     * @param senderId the sender's ID
     * @param recipientId the recipient's ID
     * @return the created chat ID
     */
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
