package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.RegistrationResponseDTO;
import com.codefactory.reservasmsauthservice.dto.response.ClientResponseDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.service.AuthService;
import com.codefactory.reservasmsauthservice.service.ClientService;
import com.codefactory.reservasmsauthservice.service.EmailService;
import com.codefactory.reservasmsauthservice.service.EmailVerificationTokenService;
import com.codefactory.reservasmsauthservice.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de AuthService.
 * Orquesta el registro de usuarios (cliente o proveedor) con generación de tokens de verificación de email.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientService clientService;
    private final ProviderService providerService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;

    @Override
    @Transactional
    public RegistrationResponseDTO registerClient(CreateClientRequestDTO request) {
        // Crear cliente
        ClientResponseDTO client = clientService.createClient(request);

        // Generar token de verificación de email
        String verificationToken = emailVerificationTokenService.generateToken(
            clientService.getUserEntityByEmail(client.getEmail())
        );

        // Enviar email de verificación
        emailService.sendVerificationEmail(client.getEmail(), client.getNombre(), verificationToken);

        return RegistrationResponseDTO.builder()
                .verificationToken(verificationToken)
                .email(client.getEmail())
                .userType("CLIENTE")
                .userId(client.getIdUsuario())
                .message("Por favor, verifica tu email para activar tu cuenta. El enlace expira en 24 horas.")
                .build();
    }

    @Override
    @Transactional
    public RegistrationResponseDTO registerProvider(CreateProviderRequestDTO request) {
        // Crear proveedor
        ProviderResponseDTO provider = providerService.createProvider(request);

        // Generar token de verificación de email
        String verificationToken = emailVerificationTokenService.generateToken(
            providerService.getUserEntityByEmail(provider.getEmail())
        );

        // Enviar email de verificación
        emailService.sendVerificationEmail(provider.getEmail(), provider.getNombreComercial(), verificationToken);

        return RegistrationResponseDTO.builder()
                .verificationToken(verificationToken)
                .email(provider.getEmail())
                .userType("PROVEEDOR")
                .userId(provider.getIdUsuario())
                .message("Por favor, verifica tu email para activar tu cuenta. El enlace expira en 24 horas.")
                .build();
    }
}
