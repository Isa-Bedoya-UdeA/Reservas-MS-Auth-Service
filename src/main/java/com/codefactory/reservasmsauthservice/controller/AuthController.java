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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Endpoints para registro, login, gestión de tokens y contraseñas")
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
    @Operation(
        summary = "Registrar cliente",
        description = "Crea una nueva cuenta de cliente con los datos proporcionados. Se envía un email de verificación.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del cliente a registrar",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateClientRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente", content = @Content(schema = @Schema(implementation = RegistrationResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Registrar proveedor",
        description = "Crea una nueva cuenta de proveedor con los datos proporcionados. Se envía un email de verificación.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del proveedor a registrar",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateProviderRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proveedor registrado exitosamente", content = @Content(schema = @Schema(implementation = RegistrationResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña. Retorna tokens JWT para acceso a la API.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales de login",
            required = true,
            content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "403", description = "Cuenta bloqueada o email no verificado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Renovar token",
        description = "Genera un nuevo access token usando un refresh token válido. El refresh token es rotado por seguridad.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Refresh token",
            required = true,
            content = @Content(schema = @Schema(implementation = RefreshTokenRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado exitosamente", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Refresh token revocado")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Cerrar sesión",
        description = "Revoca el refresh token del usuario, invalidando la sesión activa.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Refresh token a revocar",
            required = true,
            content = @Content(schema = @Schema(implementation = LogoutRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout exitoso"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
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
    @Operation(
        summary = "Solicitar restablecimiento de contraseña",
        description = "Envía un email con un enlace seguro para restablecer la contraseña del usuario.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Email del usuario",
            required = true,
            content = @Content(schema = @Schema(implementation = PasswordResetRequestDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email enviado si el usuario existe"),
        @ApiResponse(responseCode = "429", description = "Demasiadas solicitudes (rate limiting)")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Confirmar restablecimiento de contraseña",
        description = "Establece la nueva contraseña usando el token recibido por email.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Token y nueva contraseña",
            required = true,
            content = @Content(schema = @Schema(implementation = PasswordResetConfirmDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @SecurityRequirements
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
    @Operation(
        summary = "Cambiar contraseña",
        description = "Permite a un usuario autenticado cambiar su contraseña actual por una nueva.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Contraseña actual y nueva contraseña",
            required = true,
            content = @Content(schema = @Schema(implementation = ChangePasswordDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO request) {
        MessageResponseDTO response = passwordService.changePassword(request);
        return ResponseEntity.ok(response);
    }
}
