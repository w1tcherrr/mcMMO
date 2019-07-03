package com.gmail.nossr50.config;

import java.util.List;

/**
 * This is for config validation
 */
public interface UnsafeValueValidation {
    List<String> validateKeys();

    /**
     * Prints all errors found when validating the config
     */
    default void validateEntries() {
        /*
         * Print Errors about Keys
         */

        List<String> validKeyErrors = validateKeys(); // Validate Keys

        if (validKeyErrors != null && validKeyErrors.size() > 0) {
            for (String error : validKeyErrors) {
                pluginRef.getLogger().severe(error);
            }
        }
    }
}