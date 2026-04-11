package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.VerifyEmailRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.VerificationResponseDTO;
import com.codefactory.reservasmsauthservice.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de verificación de email.
 * Maneja los endpoints para verificar emails y reenviar tokens de verificación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    /**
     * Verifica el email del usuario usando el token recibido.
     * 
     * @param request DTO con el token de verificación
     * @return Confirmación de la verificación exitosa
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO request) {
        VerificationResponseDTO response = verificationService.verifyEmail(request.getToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Reenvía un nuevo token de verificación al email del usuario.
     * Útil si el token anterior ha expirado.
     * 
     * @param email Email del usuario
     * @return Confirmación del reenvío
     */
    @PostMapping("/resend-verification-email")
    public ResponseEntity<?> resendVerificationEmail(@RequestParam String email) {
        VerificationResponseDTO response = verificationService.resendVerificationToken(email);
        return ResponseEntity.ok(response);
    }
}
