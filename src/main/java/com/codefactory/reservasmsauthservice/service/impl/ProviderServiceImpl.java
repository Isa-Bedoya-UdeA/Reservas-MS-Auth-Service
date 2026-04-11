package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Provider;
import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import com.codefactory.reservasmsauthservice.mapper.ProviderMapper;
import com.codefactory.reservasmsauthservice.repository.ProviderRepository;
import com.codefactory.reservasmsauthservice.service.ProviderService;
import com.codefactory.reservasmsauthservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final UserAuthService userAuthService;

    @Override
    @Transactional
    public ProviderResponseDTO createProvider(CreateProviderRequestDTO request) {
        // Validar email y contraseña (centralizado en UserAuthService)
        userAuthService.validateEmailAndPassword(request.getEmail(), request.getPassword());

        Provider provider = providerMapper.toEntity(request);
        // Codificar contraseña (centralizado en UserAuthService)
        provider.setPasswordHash(userAuthService.encodePassword(request.getPassword()));
        
        Provider savedProvider = providerRepository.save(provider);
        return providerMapper.toDto(savedProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByEmail(String email) {
        return providerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con email '" + email + "' no encontrado"));
    }
}
