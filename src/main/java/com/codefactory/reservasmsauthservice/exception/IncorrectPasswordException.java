package com.codefactory.reservasmsauthservice.exception;

/**
 * Excepción lanzada cuando la contraseña actual proporcionada es incorrecta.
 */
public class IncorrectPasswordException extends BusinessException {
    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException() {
        super("La contraseña actual es incorrecta");
    }
}
