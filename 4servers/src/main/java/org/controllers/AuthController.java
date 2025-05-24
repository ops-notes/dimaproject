package org.dimaservers.controllers;

import io.javalin.http.Context;
import org.dimaservers.models.*;
import org.dimaservers.services.AuthService;

public class AuthController {
    private final AuthService authService;

    public void register(Context ctx) {
        AuthRequest request = ctx.bodyAsClass(AuthRequest.class);
        AuthResponse response = authService.register(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getLogin(),
                request.getPassword()
        );
        ctx.json(response).status(201);
    }

    public void login(Context ctx) {
        AuthRequest request = ctx.bodyAsClass(AuthRequest.class);
        AuthResponse response = authService.login(
                request.getLogin(),
                request.getPassword()
        );
