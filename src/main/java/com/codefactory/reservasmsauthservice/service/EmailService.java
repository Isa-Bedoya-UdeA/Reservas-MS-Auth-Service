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
}
