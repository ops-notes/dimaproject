package org.dimaservers.services;

import org.dimaservers.models.*;
import org.dimaservers.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;

    public AuthResponse register(String firstName, String lastName,
                                 String email, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setLogin(login);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

        userRepo.save(user);
        return new AuthResponse(jwtService.generateToken(user.getId()), user.getId());
    }

    public AuthResponse login(String login, String password) {
        User user = userRepo.findByLogin(login);
        if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(user.getId()), user.getId());
    }
}