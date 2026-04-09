package com.codefactory.reservasmsauthservice.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio para cargar detalles del usuario desde la base de datos.
 * NOTA: En esta versión simplificada, retorna un usuario vacío.
 * En la implementación real, se consultará la entidad USUARIO.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO: Implementar consulta real a la base de datos en Sprint 2
        // Por ahora, retornamos un usuario vacío para que compile
        return User.builder()
                .username(email)
                .password("")
                .roles("USER")
                .build();
    }
}