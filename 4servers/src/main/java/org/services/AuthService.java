package org.dimaservers.services;

import org.dimaservers.models.User;
import org.dimaservers.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService() {
        this.userRepository = new UserRepository();
        this.jwtService = new JwtService();
    }

    public int register(User user) {
        // Hash password
        user.setPasswordHash(BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    public String login(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            return jwtService.generateToken(user.getId());
        }
        throw new RuntimeException("Invalid credentials");
    }
}