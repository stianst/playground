package org.keycloak.ext.cli.oidc.oidc;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum OpenIDFlow {

    @JsonProperty("resource-owner")
    RESOURCE_OWNER,
    @JsonProperty("device")
    DEVICE

}
