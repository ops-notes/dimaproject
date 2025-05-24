package org.dimaservers;

import io.javalin.Javalin;
import org.dimaservers.controllers.*;
import org.dimaservers.services.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Инициализация
        JwtService jwtService = new JwtService();
        UserRepository userRepo = new UserRepository();
        AuthService authService = new AuthService(userRepo, jwtService);
        AuthController authController = new AuthController(authService);

        // Маршруты
        app.post("/auth/register", authController::register);
        app.post("/auth/login", authController::login);
    }
}