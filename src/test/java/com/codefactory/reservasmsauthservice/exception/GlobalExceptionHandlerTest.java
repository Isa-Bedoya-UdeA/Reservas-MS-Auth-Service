package com.codefactory.reservasmsauthservice.exception;

import com.codefactory.reservasmsauthservice.dto.response.ErrorResponseDTO;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/auth/test");
    }

    @Test
    @DisplayName("handleValidationExceptions returns 400 with field errors")
    void handleValidationExceptions() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "userRequest");
        bindingResult.addError(new FieldError("userRequest", "email", "must not be blank"));
        bindingResult.addError(new FieldError("userRequest", "password", "size must be >= 8"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidationExceptions(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Validation Error");
        assertThat(response.getBody().getValidationErrors()).hasSize(2);
        assertThat(response.getBody().getValidationErrors()).containsKeys("email", "password");
    }

    @Test
    @DisplayName("handleMessageNotReadableException for invalid UUID format")
    void handleMessageNotReadableException_InvalidUuid() {
        InvalidFormatException ife = mock(InvalidFormatException.class);
        when(ife.getTargetType()).thenReturn((Class) UUID.class);
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(ife);

        ResponseEntity<ErrorResponseDTO> response = handler.handleMessageNotReadableException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("UUID");
    }

    @Test
    @DisplayName("handleMessageNotReadableException for other invalid format")
    void handleMessageNotReadableException_OtherFormat() {
        InvalidFormatException ife = mock(InvalidFormatException.class);
        when(ife.getTargetType()).thenReturn((Class) Integer.class);
        when(ife.getPath()).thenReturn(List.of(mock(com.fasterxml.jackson.databind.JsonMappingException.Reference.class)));
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(ife);

        ResponseEntity<ErrorResponseDTO> response = handler.handleMessageNotReadableException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("Formato inválido");
    }

    @Test
    @DisplayName("handleMessageNotReadableException without cause uses default message")
    void handleMessageNotReadableException_NoCause() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(null);

        ResponseEntity<ErrorResponseDTO> response = handler.handleMessageNotReadableException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("Formato de datos inválido");
    }

    @Test
    @DisplayName("handleEmailAlreadyExistsException returns 409")
    void handleEmailAlreadyExistsException() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("test@email.com");

        ResponseEntity<ErrorResponseDTO> response = handler.handleEmailAlreadyExistsException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getError()).isEqualTo("Conflict");
    }

    @Test
    @DisplayName("handleResourceNotFoundException returns 404")
    void handleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        ResponseEntity<ErrorResponseDTO> response = handler.handleResourceNotFoundException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleInvalidCredentialsException returns 401")
    void handleInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidCredentialsException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("handleAccountLockedException returns 423")
    void handleAccountLockedException() {
        AccountLockedException ex = new AccountLockedException("Account locked");

        ResponseEntity<ErrorResponseDTO> response = handler.handleAccountLockedException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
        assertThat(response.getBody().getStatus()).isEqualTo(423);
    }

    @Test
    @DisplayName("handleEmailNotVerifiedException returns 403")
    void handleEmailNotVerifiedException() {
        EmailNotVerifiedException ex = new EmailNotVerifiedException("Email not verified");

        ResponseEntity<ErrorResponseDTO> response = handler.handleEmailNotVerifiedException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().getStatus()).isEqualTo(403);
    }

    @Test
    @DisplayName("handleInvalidVerificationTokenException returns 400 when not expired")
    void handleInvalidVerificationTokenException_NotExpired() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Invalid token", false);

        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidVerificationTokenException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("Invalid Token");
    }

    @Test
    @DisplayName("handleInvalidVerificationTokenException returns 410 when expired")
    void handleInvalidVerificationTokenException_Expired() {
        InvalidVerificationTokenException ex = new InvalidVerificationTokenException("Token expired", true);

        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidVerificationTokenException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(response.getBody().getError()).isEqualTo("Token Expired");
    }

    @Test
    @DisplayName("handleMissingServletRequestParameterException returns 400")
    void handleMissingServletRequestParameterException() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("categoryId", "String");

        ResponseEntity<ErrorResponseDTO> response = handler.handleMissingServletRequestParameterException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("categoryId");
    }

    @Test
    @DisplayName("handleTokenException returns 401")
    void handleTokenException() {
        TokenException ex = new TokenException("Token error");

        ResponseEntity<ErrorResponseDTO> response = handler.handleTokenException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("handleCategoryNotFoundException returns 400")
    void handleCategoryNotFoundException() {
        CategoryNotFoundException ex = new CategoryNotFoundException("Category not found");

        ResponseEntity<ErrorResponseDTO> response = handler.handleCategoryNotFoundException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleInvalidPasswordException returns 400")
    void handleInvalidPasswordException() {
        InvalidPasswordException ex = new InvalidPasswordException("Password too weak");

        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidPasswordException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleInvalidResetTokenException returns 410")
    void handleInvalidResetTokenException() {
        InvalidResetTokenException ex = new InvalidResetTokenException("Reset token invalid");

        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidResetTokenException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GONE);
    }

    @Test
    @DisplayName("handleIncorrectPasswordException returns 401")
    void handleIncorrectPasswordException() {
        IncorrectPasswordException ex = new IncorrectPasswordException("Wrong password");

        ResponseEntity<ErrorResponseDTO> response = handler.handleIncorrectPasswordException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("handleSamePasswordException returns 400")
    void handleSamePasswordException() {
        SamePasswordException ex = new SamePasswordException("Same as old");

        ResponseEntity<ErrorResponseDTO> response = handler.handleSamePasswordException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleBusinessException returns 400")
    void handleBusinessException() {
        BusinessException ex = new BusinessException("Business error");

        ResponseEntity<ErrorResponseDTO> response = handler.handleBusinessException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("handleIllegalStateException returns 409")
    void handleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Illegal state");

        ResponseEntity<ErrorResponseDTO> response = handler.handleIllegalStateException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("handleGenericException returns 500")
    void handleGenericException() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponseDTO> response = handler.handleGenericException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).contains("inesperado");
    }
}
