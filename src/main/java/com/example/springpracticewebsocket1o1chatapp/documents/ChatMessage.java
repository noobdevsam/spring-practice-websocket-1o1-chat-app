package com.example.springpracticewebsocket1o1chatapp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a chat message document in the database.
 */
@Document
public class ChatMessage {

    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     */
    public ChatMessage() {
    }

    /**
     * Constructs a new ChatMessage with the given details.
     *
     * @param id          the message ID
     * @param senderId    the sender's ID
     * @param chatId      the chat ID
     * @param recipientId the recipient's ID
     * @param content     the message content
     * @param timestamp   the time the message was sent
     */
    public ChatMessage(String id, String senderId, String chatId, String recipientId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.chatId = chatId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * Gets the message ID.
     * @return the message ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the message ID.
     * @param id the message ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the chat ID.
     * @return the chat ID
     */
    public String getChatId() {
        return chatId;
    }

    /**
     * Sets the chat ID.
     * @param chatId the chat ID
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * Gets the sender's ID.
     * @return the sender's ID
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Sets the sender's ID.
     * @param senderId the sender's ID
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * Gets the recipient's ID.
     * @return the recipient's ID
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Sets the recipient's ID.
     * @param recipientId the recipient's ID
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * Gets the message content.
     * @return the message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the message content.
     * @param content the message content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the timestamp of the message.
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the message.
     * @param timestamp the timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Checks if this chat message is equal to another object.
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChatMessage that)) return false;

        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(senderId, that.senderId) && Objects.equals(recipientId, that.recipientId) && Objects.equals(content, that.content) && Objects.equals(timestamp, that.timestamp);
    }

    /**
     * Generates a hash code for this chat message.
     * @return the hash code
     */
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
