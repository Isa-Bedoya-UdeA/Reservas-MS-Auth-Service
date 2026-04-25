package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.external.ExternalProviderDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;

import java.util.UUID;

public interface ProviderService {
    ProviderResponseDTO createProvider(CreateProviderRequestDTO request);
    User getUserEntityByEmail(String email);
    ExternalProviderDTO getExternalProviderById(UUID id);
}
