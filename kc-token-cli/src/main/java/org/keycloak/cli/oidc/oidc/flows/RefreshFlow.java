package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.exceptions.TokenRequestFailure;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;

public class RefreshFlow extends AbstractFlow {

    private String refreshToken;

    public RefreshFlow(String refreshToken, Context configuration, WellKnown wellKnown) {
        super(configuration, wellKnown);
        this.refreshToken = refreshToken;
    }

    public TokenResponse execute() throws OpenIDException {
        try {
            return clientRequest(wellKnown.getTokenEndpoint())
                    .accept(MimeType.JSON)
                    .contentType(MimeType.FORM)
                    .body("grant_type", "refresh_token")
                    .body("refresh_token", refreshToken)
                    .body("scope", "openid")
                    .asObject(TokenResponse.class);
        } catch (IOException e) {
            throw new TokenRequestFailure(e);
        }
    }
}
