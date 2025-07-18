package com.example.springpracticewebsocket1o1chatapp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Represents a chat room document in the database.
 */
@Document
public class ChatRoom {

    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;

    /**
     * Default constructor.
     */
    public ChatRoom() {
    }

    /**
     * Constructs a new ChatRoom with the given details.
     *
     * @param id          the chat room ID
     * @param chatId      the chat ID
     * @param senderId    the sender's ID
     * @param recipientId the recipient's ID
     */
    public ChatRoom(String id, String chatId, String senderId, String recipientId) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    /**
     * Checks if this chat room is equal to another object.
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChatRoom chatRoom)) return false;

        return Objects.equals(id, chatRoom.id) && Objects.equals(chatId, chatRoom.chatId) && Objects.equals(senderId, chatRoom.senderId) && Objects.equals(recipientId, chatRoom.recipientId);
    }


    /**
     * Gets the chat room ID.
     * @return the chat room ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the chat room ID.
     * @param id the chat room ID
     */
    public void setId(String id) {
        this.id = id;
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
     * Generates a hash code for this chat room.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(chatId);
        result = 31 * result + Objects.hashCode(senderId);
        result = 31 * result + Objects.hashCode(recipientId);
        return result;
    }

}
