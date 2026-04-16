package com.codefactory.reservasmsauthservice.service;

/**
 * Service interface for email operations.
 * Handles sending verification emails to users.
 */
public interface EmailService {

    /**
     * Sends a verification email to the user.
     *
     * @param to Recipient email address
     * @param name User's name for personalization
     * @param token Verification token to include in the email
     */
    void sendVerificationEmail(String to, String name, String token);

    /**
     * Sends a password reset email to the user.
     *
     * @param to Recipient email address
     * @param name User's name for personalization
     * @param token Password reset token to include in the email
     */
    void sendPasswordResetEmail(String to, String name, String token);

    /**
     * Sends a password change confirmation email to the user.
     *
     * @param to Recipient email address
     * @param name User's name for personalization
     */
    void sendPasswordChangeConfirmationEmail(String to, String name);
}
