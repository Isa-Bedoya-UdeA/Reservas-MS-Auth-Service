package com.codefactory.reservasmsauthservice.service.impl;

import com.codefactory.reservasmsauthservice.entity.User;
import com.codefactory.reservasmsauthservice.repository.UserRepository;
import com.codefactory.reservasmsauthservice.security.JwtService;
import com.codefactory.reservasmsauthservice.service.UserService;
import com.codefactory.reservasmsauthservice.dto.response.UserResponseDTO;
import com.codefactory.reservasmsauthservice.mapper.UserMapper;
import com.codefactory.reservasmsauthservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "test@example.com";

    private User testUser;
    private UserResponseDTO testUserResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setTipoUsuario(User.Role.CLIENTE);
        testUser.setEstado("ACTIVO");

        testUserResponse = new UserResponseDTO();
        testUserResponse.setIdUsuario(TEST_USER_ID);
        testUserResponse.setEmail(TEST_EMAIL);
        testUserResponse.setTipoUsuario("CLIENTE");
    }

    @Nested
    @DisplayName("findByEmail")
    class FindByEmailTests {

        @Test
        @DisplayName("Debe retornar usuario por email exitosamente")
        void findByEmail_Success() {
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testUserResponse);

            UserResponseDTO result = userService.findByEmail(TEST_EMAIL);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("Debe lanzar excepción si no encuentra usuario")
        void findByEmail_NotFound() {
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findByEmail(TEST_EMAIL))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado");
        }
    }

    @Nested
    @DisplayName("existsByEmail")
    class ExistsByEmailTests {

        @Test
        @DisplayName("Debe retornar true cuando el email existe")
        void existsByEmail_ReturnsTrue() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            boolean result = userService.existsByEmail(TEST_EMAIL);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Debe retornar false cuando el email no existe")
        void existsByEmail_ReturnsFalse() {
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

            boolean result = userService.existsByEmail(TEST_EMAIL);

            assertThat(result).isFalse();
        }
    }
}