package com.codefactory.reservasmsauthservice.exception;

/**
 * Excepción lanzada cuando la nueva contraseña es igual a la actual.
 */
public class SamePasswordException extends BusinessException {
    public SamePasswordException(String message) {
        super(message);
    }

    public SamePasswordException() {
        super("La nueva contraseña no puede ser igual a la actual");
    }
}
