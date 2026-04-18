package com.codefactory.reservasmsauthservice.mapper;

import com.codefactory.reservasmsauthservice.dto.request.CreateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.request.UpdateAdminRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.AdminResponseDTO;
import com.codefactory.reservasmsauthservice.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para Admin utilizando MapStruct.
 * Automatiza la conversión entre DTOs y entidades de administrador.
 */
@Mapper(componentModel = "spring")
public interface AdminMapper {
    
    @Mapping(target = "tipoUsuario", constant = "ADMIN")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerificado", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaAsignacion", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "creadoPor", ignore = true)
    Admin toEntity(CreateAdminRequestDTO dto);

    @Mapping(target = "tipoUsuario", expression = "java(entity.getTipoUsuario() != null ? entity.getTipoUsuario().name() : null)")
    AdminResponseDTO toDto(Admin entity);

    @Mapping(target = "tipoUsuario", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "emailVerificado", ignore = true)
    @Mapping(target = "intentosFallidos", ignore = true)
    @Mapping(target = "bloqueadoHasta", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaAsignacion", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    Admin updateEntityFromDto(UpdateAdminRequestDTO dto, @MappingTarget Admin entity);
}
