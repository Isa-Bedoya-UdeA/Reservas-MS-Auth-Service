package com.codefactory.reservasmsauthservice.security;

import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.BusinessException;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import org.springframework.context.annotation.Lazy;
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
    private final UserAuthService userAuthService;

    public CustomUserDetailsService(UserRepository userRepository, @Lazy UserAuthService userAuthService) {
        this.userRepository = userRepository;
        this.userAuthService = userAuthService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Delegar verificación de email verificado al servicio
        try {
            userAuthService.validateEmailVerification(user);
        } catch (BusinessException e) {
            throw new UsernameNotFoundException(e.getMessage());
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