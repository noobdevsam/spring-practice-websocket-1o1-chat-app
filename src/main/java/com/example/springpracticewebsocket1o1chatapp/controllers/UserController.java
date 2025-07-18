package com.example.springpracticewebsocket1o1chatapp.controllers;

import com.example.springpracticewebsocket1o1chatapp.documents.User;
import com.example.springpracticewebsocket1o1chatapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for handling user-related operations.
 */
@Controller
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new UserController with the given user service.
     *
     * @param userService the service to manage users
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Adds a new user to the system.
     * The user is saved and a notification is sent to the public user topic.
     * @param user the user to add
     * @return the added user
     */
    @MessageMapping("/user/add")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
        userService.saveUser(user);
        return user;
    }

    /**
     * Disconnects a user from the system.
     * The user's status is updated and a notification is sent to the public user topic.
     * @param user the user to disconnect
     * @return the disconnected user
     */
    @MessageMapping("/user/disconnect")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    /**
     * Retrieves a list of all connected users.
     * @return a list of connected users
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

}
