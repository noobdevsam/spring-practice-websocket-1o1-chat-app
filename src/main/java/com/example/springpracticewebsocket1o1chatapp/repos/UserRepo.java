package com.example.springpracticewebsocket1o1chatapp.repos;

import com.example.springpracticewebsocket1o1chatapp.documents.User;
import com.example.springpracticewebsocket1o1chatapp.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository for managing users in the database.
 */
public interface UserRepo extends MongoRepository<User, String> {

    /**
     * Finds all users with a given status.
     *
     * @param status the status to search for
     * @return a list of users with the specified status
     */
    List<User> findAllByStatus(Status status);

}
