package it.getinsight.core.util;

import java.util.Locale;

/**
 * Utility class for email normalization.
 * Centralizes email normalization logic to ensure consistency across the application.
 */
public final class EmailNormalizationUtil {

    private EmailNormalizationUtil() {
        // Utility class
    }

    /**
     * Normalizes an email address by trimming whitespace and converting to lowercase.
     *
     * @param email the email address to normalize
     * @return the normalized email, or empty string if input is null
     */
    public static String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
