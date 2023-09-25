package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

public class ResourceOwnerFlow extends AbstractFlow {

    public ResourceOwnerFlow(Context configuration, WellKnown wellKnown) {
        super(configuration, wellKnown);
    }

    @Override
    public TokenResponse execute() {
        return clientRequest(wellKnown.getTokenEndpoint())
                .accept(MimeType.JSON)
                .contentType(MimeType.FORM)
                .body("grant_type", "password")
                .body("scope", "openid")
                .body("username", configuration.getUsername())
                .body("password", configuration.getUserPassword())
                .asObject(TokenResponse.class);
    }
}
