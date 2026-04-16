package com.codefactory.reservasmsauthservice.exception;

/**
 * Excepción lanzada cuando el token de reset de contraseña es inválido, expirado o ya usado.
 */
public class InvalidResetTokenException extends BusinessException {
    public InvalidResetTokenException(String message) {
        super(message);
    }

    public InvalidResetTokenException() {
        super("El token de reset de contraseña es inválido, ha expirado o ya fue usado");
    }
}
