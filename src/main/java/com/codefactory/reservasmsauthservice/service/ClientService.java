package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ClientResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;

public interface ClientService {
    ClientResponseDTO createClient(CreateClientRequestDTO request);
    User getUserEntityByEmail(String email);
}
