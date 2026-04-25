package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.external.ExternalClientDTO;
import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ClientResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;

import java.util.UUID;

public interface ClientService {
    ClientResponseDTO createClient(CreateClientRequestDTO request);
    User getUserEntityByEmail(String email);
    ExternalClientDTO getExternalClientById(UUID id);
}
