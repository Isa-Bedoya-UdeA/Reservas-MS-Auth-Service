package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;

public interface ProviderService {
    ProviderResponseDTO createProvider(CreateProviderRequestDTO request);
    User getUserEntityByEmail(String email);
}
