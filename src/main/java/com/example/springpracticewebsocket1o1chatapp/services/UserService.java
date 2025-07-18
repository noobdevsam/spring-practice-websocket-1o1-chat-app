package com.example.springpracticewebsocket1o1chatapp.services;

import com.example.springpracticewebsocket1o1chatapp.documents.User;

import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Saves a user.
     *
     * @param user the user to save
     */
    void saveUser(User user);

    /**
     * Disconnects a user.
     * @param user the user to disconnect
     */
    void disconnect(User user);

    /**
     * Finds all connected users.
     * @return a list of connected users
     */
    List<User> findConnectedUsers();

}
