package org.dimaservers.models;

public class AuthResponse {
    private int userId;
    private String token;
    private String message;

    public AuthResponse(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getToken() { return token; }
    public String getMessage() { return message; }
}