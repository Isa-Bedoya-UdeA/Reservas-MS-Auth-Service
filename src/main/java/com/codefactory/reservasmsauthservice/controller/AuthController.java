package com.codefactory.reservasmsauthservice.controller;

import com.codefactory.reservasmsauthservice.dto.request.ChangePasswordDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.LoginRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.LogoutRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.PasswordResetConfirmDTO;
import com.codefactory.reservasmsauthservice.dto.request.PasswordResetRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.RefreshTokenRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.LoginResponseDTO;
import com.codefactory.reservasmsauthservice.dto.response.MessageResponseDTO;
import com.codefactory.reservasmsauthservice.dto.response.RegistrationResponseDTO;
import com.codefactory.reservasmsauthservice.service.AuthService;
import com.codefactory.reservasmsauthservice.service.LoginService;
import com.codefactory.reservasmsauthservice.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación.
 * Maneja los endpoints de registro de usuarios (cliente y proveedor)
 * con verificación por email, login, refresh token, logout y gestión de contraseñas.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LoginService loginService;
    private final PasswordService passwordService;

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

    /**
     * Inicia sesión de usuario.
     *
     * @param request Email y contraseña
     * @param httpRequest Request HTTP para capturar IP y User-Agent
     * @return Access token, refresh token y datos del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        LoginResponseDTO response = loginService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Renueva el access token usando un refresh token.
     *
     * @param request Refresh token
     * @return Nuevo access token y refresh token rotado
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        LoginResponseDTO response = loginService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Cierra sesión revocando el refresh token.
     *
     * @param request Refresh token a revocar
     * @return 200 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequestDTO request) {
        loginService.logout(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Solicita el restablecimiento de contraseña.
     * Envía un email con un enlace para restablecer la contraseña.
     *
     * @param request Email del usuario
     * @param httpRequest Request HTTP para capturar IP
     * @return 200 OK
     */
    @PostMapping("/password-reset/request")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO request,
                                                  HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        passwordService.requestPasswordReset(request, ipAddress);
        return ResponseEntity.ok(new MessageResponseDTO("Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña"));
    }

    /**
     * Confirma el restablecimiento de contraseña con un token válido.
     *
     * @param request Token y nueva contraseña
     * @return 200 OK con mensaje de éxito
     */
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDTO request) {
        MessageResponseDTO response = passwordService.confirmPasswordReset(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Cambia la contraseña de un usuario autenticado.
     * Requiere JWT válido en el header Authorization.
     *
     * @param request Contraseña actual y nueva contraseña
     * @return 200 OK con mensaje de éxito
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO request) {
        MessageResponseDTO response = passwordService.changePassword(request);
        return ResponseEntity.ok(response);
    }
}
