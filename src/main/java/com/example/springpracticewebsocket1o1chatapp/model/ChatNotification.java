package com.example.springpracticewebsocket1o1chatapp.model;

public record ChatNotification(
        String id,
        String senderId,
        String recipientId,
        String content

) {
}
