package com.example.springpracticewebsocket1o1chatapp.services;

import com.example.springpracticewebsocket1o1chatapp.documents.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    void disconnect(User user);

    List<User> findConnectedUsers();

}
