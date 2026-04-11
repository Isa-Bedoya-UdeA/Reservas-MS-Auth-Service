package com.codefactory.reservasmsauthservice.mapper;

import com.codefactory.reservasmsauthservice.dto.response.UserResponseDTO;
import com.codefactory.reservasmsauthservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para User utilizando MapStruct.
 * Automatiza la conversión entre entidades y DTOs de usuario.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "tipoUsuario", expression = "java(user.getTipoUsuario() != null ? user.getTipoUsuario().name() : null)")
    UserResponseDTO toDto(User user);
}
