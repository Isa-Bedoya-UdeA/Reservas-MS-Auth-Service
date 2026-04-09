package com.codefactory.reservasmsauthservice.service;

import io.jsonwebtoken.Claims;
import java.util.function.Function;

public interface JwtService {

    // Generación de tokens (SOLO en AUTH)
    String generateToken(String email, String role);
    String generateRefreshToken(String email);

    // Validación y extracción (todos los MS)
    String extractUsername(String token);
    boolean isTokenValid(String token, String userEmail);
    boolean isRefreshTokenValid(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
}