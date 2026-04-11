package com.codefactory.reservasmsauthservice.mapper;

import com.codefactory.reservasmsauthservice.dto.request.CreateProviderRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.ProviderResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Provider;
import com.codefactory.reservasmsauthservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para Provider utilizando MapStruct.
 * Automatiza la conversión entre DTOs y entidades de proveedor.
 */
@Mapper(componentModel = "spring")
public interface ProviderMapper {

    @Mapping(target = "tipoUsuario", constant = "PROVEEDOR")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerificado", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Provider toEntity(CreateProviderRequestDTO dto);

    @Mapping(target = "tipoUsuario", expression = "java(entity.getTipoUsuario() != null ? entity.getTipoUsuario().name() : null)")
    ProviderResponseDTO toDto(Provider entity);
}
