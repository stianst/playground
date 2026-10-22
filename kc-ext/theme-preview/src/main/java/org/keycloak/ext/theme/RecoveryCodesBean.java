package org.keycloak.ext.theme;

import org.keycloak.common.util.Time;
import org.keycloak.models.utils.RecoveryAuthnCodesUtils;

import java.util.List;

/**
 * Custom bean for recovery codes that provides the property names
 * expected by the login-spa theme template.
 */
public class RecoveryCodesBean {

    private final List<String> generatedRecoveryAuthnCodes;
    private final long generatedAt;

    public RecoveryCodesBean() {
        this.generatedRecoveryAuthnCodes = RecoveryAuthnCodesUtils.generateRawCodes();
        this.generatedAt = Time.currentTimeMillis();
    }

    // Property name expected by template: generatedRecoveryAuthnCodes
    public List<String> getGeneratedRecoveryAuthnCodes() {
        return this.generatedRecoveryAuthnCodes;
    }

    // Also provide the List suffix version for compatibility
    public List<String> getGeneratedRecoveryAuthnCodesList() {
        return this.generatedRecoveryAuthnCodes;
    }

    public String getGeneratedRecoveryAuthnCodesAsString() {
        return String.join(",", this.generatedRecoveryAuthnCodes);
    }

    public long getGeneratedAt() {
        return generatedAt;
    }
}
