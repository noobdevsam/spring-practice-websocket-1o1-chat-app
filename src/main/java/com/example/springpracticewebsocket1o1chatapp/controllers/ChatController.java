package com.example.springpracticewebsocket1o1chatapp.controllers;

import com.example.springpracticewebsocket1o1chatapp.documents.ChatMessage;
import com.example.springpracticewebsocket1o1chatapp.model.ChatNotification;
import com.example.springpracticewebsocket1o1chatapp.services.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controller for handling chat-related operations.
 */
@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    /**
     * Constructs a new ChatController with the given messaging template and chat message service.
     *
     * @param simpMessagingTemplate the messaging template to send messages
     * @param chatMessageService    the service to manage chat messages
     */
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatMessageService chatMessageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    /**
     * Processes a chat message sent by a user.
     * The message is saved and then sent to the recipient's message queue.
     * @param chatMessage the chat message to process
     */
    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ) {
        var savedMessage = chatMessageService.save(chatMessage);

        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        savedMessage.getId(),
                        savedMessage.getSenderId(),
                        savedMessage.getRecipientId(),
                        savedMessage.getContent()
                )
        );
    }

    /**
     * Retrieves the chat messages between two users.
     * @param senderId the ID of the sender
     * @param recipientId the ID of the recipient
     * @return a list of chat messages between the two users
     */
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

}
