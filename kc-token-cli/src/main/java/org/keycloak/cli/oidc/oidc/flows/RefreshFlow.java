package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

public class RefreshFlow extends AbstractFlow {

    private String refreshToken;

    public RefreshFlow(String refreshToken, Context configuration, WellKnown wellKnown) {
        super(configuration, wellKnown);
        this.refreshToken = refreshToken;
    }

    public TokenResponse execute() {
        return clientRequest(wellKnown.getTokenEndpoint())
                .accept(MimeType.JSON)
                .contentType(MimeType.FORM)
                .body("grant_type", "refresh_token")
                .body("refresh_token", refreshToken)
                .body("scope", "openid")
                .asObject(TokenResponse.class);
    }
}
