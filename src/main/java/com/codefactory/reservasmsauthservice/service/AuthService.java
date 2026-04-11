package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.RegistrationResponseDTO;

public interface AuthService {
    RegistrationResponseDTO registerClient(CreateClientRequestDTO request);
    RegistrationResponseDTO registerProvider(CreateProviderRequestDTO request);
}
