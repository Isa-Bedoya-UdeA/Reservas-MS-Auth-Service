package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.CreateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.UpdateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.AdminResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    AdminResponseDTO createAdmin(CreateAdminRequestDTO request, UUID creadoPor);
    AdminResponseDTO initializeFirstAdmin(CreateAdminRequestDTO request);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO getAdminById(UUID id);
    AdminResponseDTO updateAdmin(UUID id, UpdateAdminRequestDTO request);
    void deactivateAdmin(UUID id);
    void activateAdmin(UUID id);
    User getUserEntityByEmail(String email);
}
