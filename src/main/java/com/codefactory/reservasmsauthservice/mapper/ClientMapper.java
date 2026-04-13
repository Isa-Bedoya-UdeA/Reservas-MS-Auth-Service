package com.codefactory.reservasmsauthservice.mapper;

import com.codefactory.reservasmsauthservice.dto.request.CreateClientRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ClientResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para Client utilizando MapStruct.
 * Automatiza la conversión entre DTOs y entidades de cliente.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {
    
    @Mapping(target = "tipoUsuario", constant = "CLIENTE")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerificado", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Client toEntity(CreateClientRequestDTO dto);

    @Mapping(target = "tipoUsuario", expression = "java(entity.getTipoUsuario() != null ? entity.getTipoUsuario().name() : null)")
    ClientResponseDTO toDto(Client entity);
}
