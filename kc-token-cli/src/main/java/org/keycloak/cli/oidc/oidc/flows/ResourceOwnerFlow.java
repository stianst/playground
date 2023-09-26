package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.TokenRequestFailure;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;

public class ResourceOwnerFlow extends AbstractFlow {

    public ResourceOwnerFlow(Context configuration, WellKnown wellKnown) {
        super(configuration, wellKnown);
    }

    @Override
    public TokenResponse execute() throws TokenRequestFailure {
        try {
            return clientRequest(wellKnown.getTokenEndpoint())
                    .accept(MimeType.JSON)
                    .contentType(MimeType.FORM)
                    .body("grant_type", "password")
                    .body("scope", "openid")
                    .body("username", context.getUsername())
                    .body("password", context.getUserPassword())
                    .asObject(TokenResponse.class);
        } catch (IOException e) {
            throw new TokenRequestFailure(e);
        }
    }
}
