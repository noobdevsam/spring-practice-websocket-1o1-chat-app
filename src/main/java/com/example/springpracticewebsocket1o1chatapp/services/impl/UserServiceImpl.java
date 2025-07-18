package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.User;
import com.example.springpracticewebsocket1o1chatapp.model.Status;
import com.example.springpracticewebsocket1o1chatapp.repos.UserRepo;
import com.example.springpracticewebsocket1o1chatapp.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    /**
     * Constructs a new UserServiceImpl with the given user repository.
     *
     * @param userRepo the user repository
     */
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Saves a user and sets their status to ONLINE.
     * @param user the user to save
     */
    @Override
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepo.save(user);
    }

    /**
     * Disconnects a user by setting their status to OFFLINE.
     * It finds the user in the repository before updating their status.
     * @param user the user to disconnect
     */
    @Override
    public void disconnect(User user) {
        var storedUser = userRepo.findById(
                user.getNickName()
        ).orElse(null);

        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepo.save(storedUser);
        }
    }

    /**
     * Finds all users who are currently online.
     * @return a list of connected (online) users
     */
    @Override
    public List<User> findConnectedUsers() {
        return userRepo.findAllByStatus(Status.ONLINE);
    }

}
