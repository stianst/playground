package org.keycloak.cli.oidc.oidc;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum OpenIDFlow {

    @JsonProperty("authorization-code")
    AUTHORIZATION_CODE,
    @JsonProperty("resource-owner")
    RESOURCE_OWNER,
    @JsonProperty("device")
    DEVICE,
    @JsonProperty("client-credential")
    CLIENT_CREDENTIAL

}
