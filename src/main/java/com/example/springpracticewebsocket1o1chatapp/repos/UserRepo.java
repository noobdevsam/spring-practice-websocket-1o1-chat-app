package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.User;
import com.example.springpracticewebsocket1o1chatapp.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {

    List<User> findAllByStatus(Status status);

}
