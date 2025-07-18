package com.example.springpracticewebsocket1o1chatapp.documents;

import com.example.springpracticewebsocket1o1chatapp.model.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user document in the database.
 */
@Document
public class User {

    @Id
    private String nickName;
    private String fullName;
    private Status status;

    /**
     * Gets the user's nickname.
     *
     * @return the nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets the user's nickname.
     * @param nickName the nickname
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets the user's full name.
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the user's full name.
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the user's status.
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the user's status.
     * @param status the status
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
