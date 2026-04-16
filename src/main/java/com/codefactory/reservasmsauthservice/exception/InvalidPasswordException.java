package com.codefactory.reservasmsauthservice.exception;

/**
 * Excepción lanzada cuando la contraseña no cumple con los requisitos de formato.
 */
public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException() {
        super("La contraseña no cumple con los requisitos de formato");
    }
}
