package com.example.springpracticewebsocket1o1chatapp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document
public class ChatMessage {

    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String id, String senderId, String chatId, String recipientId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.chatId = chatId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChatMessage that)) return false;

        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(senderId, that.senderId) && Objects.equals(recipientId, that.recipientId) && Objects.equals(content, that.content) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(chatId);
        result = 31 * result + Objects.hashCode(senderId);
        result = 31 * result + Objects.hashCode(recipientId);
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(timestamp);
        return result;
    }
}
