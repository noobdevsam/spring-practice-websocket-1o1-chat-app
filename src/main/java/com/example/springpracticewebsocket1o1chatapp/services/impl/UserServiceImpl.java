package com.example.springpracticewebsocket1o1chatapp.services.impl;

import com.example.springpracticewebsocket1o1chatapp.documents.User;
import com.example.springpracticewebsocket1o1chatapp.model.Status;
import com.example.springpracticewebsocket1o1chatapp.repos.UserRepo;
import com.example.springpracticewebsocket1o1chatapp.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepo.save(user);
    }

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

    @Override
    public List<User> findConnectedUsers() {
        return userRepo.findAllByStatus(Status.ONLINE);
    }

}
