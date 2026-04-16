package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.request.LoginRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.LogoutRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.RefreshTokenRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.LoginResponseDTO;
import com.codefactory.reservasmsauthservice.entity.LoginAttempt;
import com.codefactory.reservasmsauthservice.entity.RefreshToken;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.AccountLockedException;
import com.codefactory.reservasmsauthservice.exception.EmailNotVerifiedException;
import com.codefactory.reservasmsauthservice.exception.InvalidCredentialsException;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.repository.LoginAttemptRepository;
import com.codefactory.reservasmsauthservice.repository.RefreshTokenRepository;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.security.JwtService;
import com.codefactory.reservasmsauthservice.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final JwtService jwtService;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager transactionManager;

    @Value("${login.max-attempts:5}")
    private int maxAttempts;

    @Value("${login.lockout-duration-hours:24}")
    private int lockoutDurationHours;

    private TransactionTemplate createRequiresNewTransactionTemplate() {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return tt;
    }

    private void updateFailedAttemptsBypass(UUID userId, int attempts) {
        log.info("Executing updateFailedAttemptsBypass for user: {}, attempts: {}", userId, attempts);
        TransactionTemplate tt = createRequiresNewTransactionTemplate();
        tt.executeWithoutResult(status -> {
            Boolean result = jdbcTemplate.queryForObject("SELECT actualizar_intentos_fallidos(?, ?)", Boolean.class, userId, attempts);
            log.info("actualizar_intentos_fallidos returned: {}", result);
        });
    }

    private void lockUserBypass(UUID userId, LocalDateTime lockUntil, String estado) {
        log.info("Executing lockUserBypass for user: {}, until: {}", userId, lockUntil);
        TransactionTemplate tt = createRequiresNewTransactionTemplate();
        tt.executeWithoutResult(status -> {
            Boolean result = jdbcTemplate.queryForObject("SELECT bloquear_usuario(?, ?, ?)", Boolean.class, userId, lockUntil, estado);
            log.info("bloquear_usuario returned: {}", result);
        });
    }

    private void resetFailedAttemptsBypass(UUID userId) {
        log.info("Executing resetFailedAttemptsBypass for user: {}", userId);
        TransactionTemplate tt = createRequiresNewTransactionTemplate();
        tt.executeWithoutResult(status -> {
            Boolean result = jdbcTemplate.queryForObject("SELECT resetear_intentos_fallidos(?)", Boolean.class, userId);
            log.info("resetear_intentos_fallidos returned: {}", result);
        });
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest) {
        String email = request.getEmail();
        String password = request.getPassword();
        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        // Buscar usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login attempt with non-existent email: {}", email);
                    return new ResourceNotFoundException("Usuario con email '" + email + "' no encontrado");
                });

        // Verificar si el email está verificado
        if (!user.getEmailVerificado()) {
            log.warn("Login attempt with unverified email: {}", email);
            throw new EmailNotVerifiedException("Debes verificar tu email antes de iniciar sesión");
        }

        // Verificar si el usuario está bloqueado y si el bloqueo aún es válido
        if (user.getBloqueadoHasta() != null && user.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            log.warn("Login attempt for locked account: {}", email);
            throw new AccountLockedException(
                    "Tu cuenta está bloqueada temporalmente. Intenta de nuevo después de " +
                            user.getBloqueadoHasta().toLocalDate() + " a las " +
                            user.getBloqueadoHasta().toLocalTime()
            );
        }

        // Si el bloqueo expiró, resetear intentos fallidos
        if (user.getBloqueadoHasta() != null && user.getBloqueadoHasta().isBefore(LocalDateTime.now())) {
            resetFailedAttemptsBypass(user.getIdUsuario());
            user.setIntentosFallidos(0);
            user.setBloqueadoHasta(null);
            user.setEstado("ACTIVO");
            log.info("Account lock expired for user: {}, resetting failed attempts", email);
        }

        // Validar contraseña
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            // Contraseña incorrecta
            int newFailedAttempts = user.getIntentosFallidos() + 1;
            updateFailedAttemptsBypass(user.getIdUsuario(), newFailedAttempts);
            user.setIntentosFallidos(newFailedAttempts);
            log.info("Updated intentos_fallidos to {} for user: {}", newFailedAttempts, user.getIdUsuario());

            // Registrar intento fallido
            LoginAttempt failedAttempt = LoginAttempt.builder()
                    .user(user)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .exitoso(false)
                    .errorMessage("Contraseña incorrecta")
                    .build();
            loginAttemptRepository.save(failedAttempt);

            log.warn("Failed login attempt for user: {}, attempts: {}", email, newFailedAttempts);

            // Verificar si se debe bloquear la cuenta
            if (newFailedAttempts >= maxAttempts) {
                LocalDateTime lockUntil = LocalDateTime.now().plusHours(lockoutDurationHours);
                lockUserBypass(user.getIdUsuario(), lockUntil, "BLOQUEADO");
                user.setBloqueadoHasta(lockUntil);
                user.setEstado("BLOQUEADO");
                log.warn("Account locked for user: {} until {}", email, lockUntil);
                throw new AccountLockedException(
                        "Demasiados intentos fallidos. Tu cuenta está bloqueada por " + lockoutDurationHours + " horas. " +
                                "Intenta de nuevo después de " + lockUntil.toLocalDate() + " a las " + lockUntil.toLocalTime()
                );
            }

            throw new InvalidCredentialsException("Correo o contraseña incorrectos");
        }

        // Login exitoso - resetear intentos fallidos
        resetFailedAttemptsBypass(user.getIdUsuario());
        user.setIntentosFallidos(0);
        user.setBloqueadoHasta(null);
        user.setEstado("ACTIVO");

        // Registrar intento exitoso
        LoginAttempt successfulAttempt = LoginAttempt.builder()
                .user(user)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .exitoso(true)
                .build();
        loginAttemptRepository.save(successfulAttempt);

        log.info("Successful login for user: {}", email);

        // Generar tokens
        String accessToken = jwtService.generateToken(user.getEmail(), user.getTipoUsuario().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // Guardar refresh token en DB
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .direccionIp(ipAddress)
                .infoDispositivo(userAgent)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return LoginResponseDTO.from(accessToken, refreshToken, user);
    }

    @Override
    @Transactional
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        // Buscar refresh token en DB
        RefreshToken refreshTokenEntity = refreshTokenRepository.findValidByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found or invalid");
                    return new InvalidCredentialsException("Refresh token inválido o expirado");
                });

        // Validar token con JWT
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            log.warn("Refresh token JWT validation failed");
            throw new InvalidCredentialsException("Refresh token inválido o expirado");
        }

        User user = refreshTokenEntity.getUser();

        // Generar nuevo access token
        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getTipoUsuario().name());

        // Rotar refresh token (opcional - crear nuevo y revocar antiguo)
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());
        refreshTokenEntity.setRevocado(true);
        refreshTokenEntity.setFechaRevocacion(LocalDateTime.now());
        refreshTokenRepository.save(refreshTokenEntity);

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(newRefreshToken)
                .direccionIp(refreshTokenEntity.getDireccionIp())
                .infoDispositivo(refreshTokenEntity.getInfoDispositivo())
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        log.info("Refresh token renewed for user: {}", user.getEmail());

        return LoginResponseDTO.from(newAccessToken, newRefreshToken, user);
    }

    @Override
    @Transactional
    public void logout(LogoutRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        // Buscar refresh token en DB
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Logout attempt with non-existent refresh token");
                    return new InvalidCredentialsException("Refresh token no encontrado");
                });

        // Revocar el token
        refreshTokenEntity.setRevocado(true);
        refreshTokenEntity.setFechaRevocacion(LocalDateTime.now());
        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User logged out, refresh token revoked for user: {}", refreshTokenEntity.getUser().getEmail());
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For puede tener múltiples IPs, tomar la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
