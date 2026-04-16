package com.codefactory.reservasmsauthservice.service;

import com.codefactory.reservasmsauthservice.dto.request.ChangePasswordDTO;
import com.codefactory.reservasmsauthservice.dto.request.PasswordResetConfirmDTO;
import com.codefactory.reservasmsauthservice.dto.request.PasswordResetRequestDTO;
import com.codefactory.reservasmsauthservice.dto.response.MessageResponseDTO;

import java.util.UUID;

/**
 * Service interface for password operations.
 * Handles password reset (forgot password) and password change (authenticated).
 */
public interface PasswordService {

    /**
     * Requests a password reset for a user by email.
     * Generates a token and sends an email with the reset link.
     *
     * @param request Contains the user's email
     * @param ipAddress IP address of the requester
     */
    void requestPasswordReset(PasswordResetRequestDTO request, String ipAddress);

    /**
     * Confirms a password reset using a valid token.
     * Updates the user's password and marks the token as used.
     *
     * @param request Contains the token and new password
     * @return Success message
     */
    MessageResponseDTO confirmPasswordReset(PasswordResetConfirmDTO request);

    /**
     * Changes the password for an authenticated user.
     * Verifies the current password and updates to the new password.
     *
     * @param request Contains current password and new password
     * @return Success message
     */
    MessageResponseDTO changePassword(ChangePasswordDTO request);
}
