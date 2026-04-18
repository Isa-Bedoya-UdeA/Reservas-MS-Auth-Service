package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.request.CreateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.UpdateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.AdminResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Admin;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.AdminMapper;
import com.codefactory.reservasmsauthservice.repository.AdminRepository;
import com.codefactory.reservasmsauthservice.service.AdminService;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserAuthService userAuthService;

    @Override
    @Transactional
    public AdminResponseDTO createAdmin(CreateAdminRequestDTO request, UUID creadoPor) {
        // Validar email y contraseña (centralizado en UserAuthService)
        userAuthService.validateEmailAndPassword(request.getEmail(), request.getPassword());

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con el email: " + request.getEmail());
        }

        Admin admin = adminMapper.toEntity(request);
        // Codificar contraseña (centralizado en UserAuthService)
        admin.setPasswordHash(userAuthService.encodePassword(request.getPassword()));
        // Establecer fecha de registro
        admin.setFechaRegistro(java.time.LocalDateTime.now());
        // Establecer fecha de asignación
        admin.setFechaAsignacion(java.time.LocalDateTime.now());
        // Establecer el creador
        admin.setCreadoPor(creadoPor);
        // Los administradores no requieren verificación de email
        admin.setEmailVerificado(true);

        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional
    public AdminResponseDTO initializeFirstAdmin(CreateAdminRequestDTO request) {
        // Verificar que no existan admins en la base de datos
        if (!adminRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Ya existen administradores en el sistema. Use el endpoint de creación regular.");
        }

        // Validar email y contraseña (centralizado en UserAuthService)
        userAuthService.validateEmailAndPassword(request.getEmail(), request.getPassword());

        Admin admin = adminMapper.toEntity(request);
        // Codificar contraseña (centralizado en UserAuthService)
        admin.setPasswordHash(userAuthService.encodePassword(request.getPassword()));
        // Establecer fecha de registro
        admin.setFechaRegistro(java.time.LocalDateTime.now());
        // Establecer fecha de asignación
        admin.setFechaAsignacion(java.time.LocalDateTime.now());
        // Los administradores no requieren verificación de email
        admin.setEmailVerificado(true);
        // Para el primer admin, el creador es null (se crea a sí mismo)
        admin.setCreadoPor(null);

        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(adminMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminById(UUID id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador con ID '" + id + "' no encontrado"));
        return adminMapper.toDto(admin);
    }

    @Override
    @Transactional
    public AdminResponseDTO updateAdmin(UUID id, UpdateAdminRequestDTO request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador con ID '" + id + "' no encontrado"));
        
        Admin updatedAdmin = adminMapper.updateEntityFromDto(request, admin);
        Admin savedAdmin = adminRepository.save(updatedAdmin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional
    public void deactivateAdmin(UUID id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador con ID '" + id + "' no encontrado"));
        admin.setActivo(false);
        adminRepository.save(admin);
    }

    @Override
    @Transactional
    public void activateAdmin(UUID id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador con ID '" + id + "' no encontrado"));
        admin.setActivo(true);
        adminRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador con email '" + email + "' no encontrado"));
    }
}
