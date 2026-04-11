package com.codefactory.reservasmsauthservice.service;

/**
 * Servicio abstrato para lógica compartida de autenticación y registro de usuarios.
 * Centraliza las operaciones comunes entre ClientService y ProviderService.
 */
public interface UserAuthService {
    /**
     * Verifica si un email ya existe y valida la contraseña.
     * @param email Email a verificar
     * @param password Contraseña a validar
     * @throws com.codefactory.reservasmsauthservice.exception.EmailAlreadyExistsException si el email ya existe
     * @throws com.codefactory.reservasmsauthservice.exception.BusinessException si la contraseña no cumple validaciones
     */
    void validateEmailAndPassword(String email, String password);

    /**
     * Codifica una contraseña usando BCrypt.
     * @param password Contraseña en texto plano
     * @return Contraseña codificada
     */
    String encodePassword(String password);
}
