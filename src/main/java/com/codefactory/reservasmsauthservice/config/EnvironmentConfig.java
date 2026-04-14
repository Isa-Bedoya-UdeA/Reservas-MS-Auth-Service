package com.codefactory.reservasmsauthservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class for debugging and validating environment variables.
 * Logs database connection details during application startup.
 */
@Configuration
public class EnvironmentConfiguration implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfiguration.class);

    private final Environment env;

    @Value("${spring.datasource.url:not-set}")
    private String dbUrl;

    @Value("${spring.datasource.username:not-set}")
    private String dbUsername;

    public EnvironmentConfiguration(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info("Environment Configuration Validation");
        logger.info("========================================");
        
        // Log active profiles
        String[] activeProfiles = env.getActiveProfiles();
        String profiles = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
        logger.info("Active Profile(s): {}", profiles);
        
        // Mask the password for security
        String maskedUrl = maskSensitiveInfo(dbUrl);
        logger.info("Database URL: {}", maskedUrl);
        logger.info("Database Username: {}", dbUsername);
        
        // Verify critical environment variables
        String jwtSecret = env.getProperty("jwt.secret");
        boolean jwtConfigured = jwtSecret != null && !jwtSecret.contains("default");
        logger.info("JWT Configuration: {}", jwtConfigured ? "CONFIGURED" : "USING DEFAULT (DEV MODE)");
        
        // Validate connection pool settings
        int maxPoolSize = env.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 10);
        int minIdle = env.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 5);
        logger.info("HikariCP Pool Configuration - Max: {}, Min Idle: {}", maxPoolSize, minIdle);
        
        logger.info("========================================");
        logger.info("Application ready to accept connections");
        logger.info("========================================");
    }

    private String maskSensitiveInfo(String url) {
        if (url == null || url.length() < 20) {
            return url;
        }
        // Mask password if present in URL (rarely used with separate credentials, but just in case)
        return url.replaceAll("password=[^&]*", "password=***");
    }
}
