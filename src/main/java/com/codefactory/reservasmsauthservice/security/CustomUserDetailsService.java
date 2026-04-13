package com.codefactory.reservasmsauthservice.security;

import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Verificar que el email haya sido verificado
        if (!Boolean.TRUE.equals(user.getEmailVerificado())) {
            throw new UsernameNotFoundException(
                "Tu email aún no ha sido verificado. Por favor, revisa tu bandeja de entrada y completa la verificación."
            );
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(Collections.singletonList(authority))
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!"ACTIVO".equalsIgnoreCase(user.getEstado()))
                .build();
    }
}