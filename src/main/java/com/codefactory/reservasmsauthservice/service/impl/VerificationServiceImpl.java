package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Client;
import com.codefactory.reservasmsauthservice.entity.EmailVerificationToken;
import com.codefactory.reservasmsauthservice.entity.Provider;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.repository.ClientRepository;
import com.codefactory.reservasmsauthservice.repository.EmailVerificationTokenRepository;
import com.codefactory.reservasmsauthservice.repository.ProviderRepository;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.service.EmailService;
import com.codefactory.reservasmsauthservice.service.EmailVerificationTokenService;
import com.codefactory.reservasmsauthservice.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de VerificationService.
 * Maneja la verificación de emails y activación de cuentas de usuario.
 */
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    @Override
    @Transactional
    public VerificationResponseDTO verifyEmail(String token) {
        // Validar y obtener el token de verificación
        EmailVerificationToken verificationToken = emailVerificationTokenService.validateToken(token);
        
        // Obtener el usuario asociado al token
        User user = verificationToken.getUser();
        
        // Marcar el token como usado
        emailVerificationTokenService.confirmToken(token);
        
        // Marcar el email del usuario como verificado
        user.setEmailVerificado(true);
        userRepository.save(user);
        
        // Retornar respuesta exitosa
        return VerificationResponseDTO.builder()
                .success(true)
                .message("¡Felicidades! Tu email ha sido verificado y tu cuenta está activa.")
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public VerificationResponseDTO resendVerificationToken(String email) {
        // Obtener el usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email '" + email + "' no encontrado"));

        // Verificar si el usuario ya está verificado
        if (Boolean.TRUE.equals(user.getEmailVerificado())) {
            return VerificationResponseDTO.builder()
                    .success(false)
                    .message("Este email ya ha sido verificado. Puedes iniciar sesión directamente.")
                    .userId(user.getIdUsuario())
                    .email(user.getEmail())
                    .build();
        }

        // Eliminar el token anterior si existe
        emailVerificationTokenRepository.deleteByUser_IdUsuario(user.getIdUsuario());

        // Generar nuevo token
        String newToken = emailVerificationTokenService.generateToken(user);

        // Obtener el nombre del usuario según su tipo
        String userName;
        if (user.getTipoUsuario() == User.Role.CLIENTE) {
            Client client = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            userName = client.getNombre();
        } else {
            Provider provider = providerRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            userName = provider.getNombreComercial();
        }

        // Enviar email con el nuevo token
        emailService.sendVerificationEmail(email, userName, newToken);

        return VerificationResponseDTO.builder()
                .success(true)
                .message("Se ha reenviado un nuevo token de verificación a tu email. El enlace expira en 24 horas.")
                .userId(user.getIdUsuario())
                .email(user.getEmail())
                .build();
    }
}
