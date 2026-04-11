package com.codefactory.reservasmsauthservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidad que representa un token de verificación de email.
 * Se genera cuando un usuario se registra y debe ser validado
 * antes de poder iniciar sesión.
 */
@Entity
@Table(name = "email_verification_token")
@Getter
@Setter
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(nullable = false)
    private Boolean used = false;
    
    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            expiryDate = LocalDateTime.now().plusHours(24);
        }
    }
}
