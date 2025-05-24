package org.dimaservers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.dimaservers.models.User;
import org.dimaservers.services.JwtService;
import org.dimaservers.services.UserService;

public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Получение данных пользователя по ID (требуется JWT)
     * GET /users/{id}
     */
    public void getUserById(Context ctx) {
        try {
            // 1. Проверка авторизации
            String token = extractToken(ctx);
            Long requestingUserId = jwtService.validateToken(token);

            // 2. Получение запрашиваемого ID
            Long requestedUserId = Long.parseLong(ctx.pathParam("id"));

            // 3. Проверка прав доступа
            if (!requestingUserId.equals(requestedUserId)) {
                ctx.status(HttpStatus.FORBIDDEN).json(Map.of("error", "Access denied"));
                return;
            }

            // 4. Получение данных
            User user = userService.getUserById(requestedUserId);
            ctx.json(Map.of(
                    "id", user.getId(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail(),
                    "login", user.getLogin()
            ));

        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error", "Invalid user ID format"));
        } catch (Exception e) {
            ctx.status(HttpStatus.UNAUTHORIZED).json(Map.of("error", "Invalid or expired token"));
        }
    }

    /**
     * Обновление данных пользователя
     * PATCH /users/{id}
     */
    public void updateUser(Context ctx) {
        try {
            String token = extractToken(ctx);
            Long userId = jwtService.validateToken(token);

            User updatedData = ctx.bodyAsClass(User.class);
            userService.updateUser(userId, updatedData);

            ctx.status(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Удаление пользователя
     * DELETE /users/{id}
     */
    public void deleteUser(Context ctx) {
        try {
            String token = extractToken(ctx);
            Long userId = jwtService.validateToken(token);

            userService.deleteUser(userId);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(Map.of("error", e.getMessage()));
        }
    }

    // Вспомогательный метод для извлечения токена
    private String extractToken(Context ctx) {
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        return header.substring(7);
    }
}