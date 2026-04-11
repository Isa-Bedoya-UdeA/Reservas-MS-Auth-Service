package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.RegistrationResponseDTO;
import com.codefactory.reservasmsauthservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación.
 * Maneja los endpoints de registro de usuarios (cliente y proveedor) 
 * con verificación por email.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registra un nuevo cliente.
     * 
     * @param request Datos del nuevo cliente (email, contraseña, nombre, etc)
     * @return Token de verificación de email a enviar a la cuenta del usuario
     */
    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody CreateClientRequestDTO request) {
        RegistrationResponseDTO response = authService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Registra un nuevo proveedor.
     * 
     * @param request Datos del nuevo proveedor (email, contraseña, nombre, etc)
     * @return Token de verificación de email a enviar a la cuenta del usuario
     */
    @PostMapping("/register/provider")
    public ResponseEntity<?> registerProvider(@Valid @RequestBody CreateProviderRequestDTO request) {
        RegistrationResponseDTO response = authService.registerProvider(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
