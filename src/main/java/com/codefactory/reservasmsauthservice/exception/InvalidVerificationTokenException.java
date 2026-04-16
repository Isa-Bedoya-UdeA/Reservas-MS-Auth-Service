package com.codefactory.reservasmsauthservice.exception;

/**
 * Excepción lanzada cuando un token de verificación de email es inválido, expirado o ya fue utilizado.
 * Devuelve código HTTP 400 Bad Request para tokens inválidos o 410 Gone para tokens expirados.
 */
public class InvalidVerificationTokenException extends RuntimeException {

    private final boolean expired;

    public InvalidVerificationTokenException(String message) {
        super(message);
        this.expired = false;
    }

    public InvalidVerificationTokenException(String message, boolean expired) {
        super(message);
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }
}
