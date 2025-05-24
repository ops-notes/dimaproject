package org.dimsservers.services;

import org.dimsservers.models.User;
import org.dimsservers.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public User getUserData(int userId) {
        return userRepository.findById(userId);
    }
}