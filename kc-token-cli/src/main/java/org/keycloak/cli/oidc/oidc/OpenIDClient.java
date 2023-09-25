package org.keycloak.cli.oidc.oidc;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.Http;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.flows.AbstractFlow;
import org.keycloak.cli.oidc.oidc.flows.DeviceFlow;
import org.keycloak.cli.oidc.oidc.flows.RefreshFlow;
import org.keycloak.cli.oidc.oidc.flows.ResourceOwnerFlow;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

public class OpenIDClient {

    private Context configuration;
    private WellKnown wellKnown;

    public OpenIDClient(Context configuration) {
        this.configuration = configuration;

        wellKnown = Http.create(configuration.getIssuer() + "/.well-known/openid-configuration")
                .accept(MimeType.JSON)
                .asObject(WellKnown.class);
    }

    public static OpenIDClient create(Context configuration) {
        return new OpenIDClient(configuration);
    }

    public TokenResponse token() {
        AbstractFlow flow;
        switch (configuration.getFlow()) {
            case RESOURCE_OWNER:
                flow = new ResourceOwnerFlow(configuration, wellKnown);
                break;
            case DEVICE:
                flow = new DeviceFlow(configuration, wellKnown);
                break;
            default:
                throw new RuntimeException("Unknown flow");
        }

        TokenResponse tokenResponse = flow.execute();
        return checkError(tokenResponse);
    }

    public TokenResponse refresh(String refreshToken) {
        TokenResponse tokenResponse = new RefreshFlow(refreshToken, configuration, wellKnown)
                .execute();
        return checkError(tokenResponse);
    }

    private TokenResponse checkError(TokenResponse tokenResponse) {
        if (tokenResponse.getError() != null) {
            throw new RuntimeException("Request failed " + tokenResponse.getError());
        }
        return tokenResponse;
    }

}
