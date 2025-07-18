package com.example.springpracticewebsocket1o1chatapp.model;

/**
 * Represents a chat notification that is sent to users.
 *
 * @param id          the notification ID
 * @param senderId    the sender's ID
 * @param recipientId the recipient's ID
 * @param content     the notification content
 */
public record ChatNotification(
        String id,
        String senderId,
        String recipientId,
        String content

) {
}
