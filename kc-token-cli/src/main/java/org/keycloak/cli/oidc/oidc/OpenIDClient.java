package org.keycloak.cli.oidc.oidc;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.Http;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.flows.AbstractFlow;
import org.keycloak.cli.oidc.oidc.flows.DeviceFlow;
import org.keycloak.cli.oidc.oidc.flows.RefreshFlow;
import org.keycloak.cli.oidc.oidc.flows.ResourceOwnerFlow;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;

public class OpenIDClient {

    private Context configuration;
    private WellKnown wellKnown;

    public OpenIDClient(Context configuration) throws OpenIDException {
        this.configuration = configuration;

        try {
            wellKnown = Http.create(configuration.getIssuer() + "/.well-known/openid-configuration")
                    .accept(MimeType.JSON)
                    .asObject(WellKnown.class);
        } catch (IOException e) {
            throw new OpenIDException("Failed to retrieve well-known endpoint", e);
        }
    }

    public static OpenIDClient create(Context configuration) throws OpenIDException {
        return new OpenIDClient(configuration);
    }

    public TokenResponse token() throws OpenIDException {
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

    public TokenResponse refresh(String refreshToken) throws OpenIDException {
        TokenResponse tokenResponse = new RefreshFlow(refreshToken, configuration, wellKnown)
                .execute();
        return checkError(tokenResponse);
    }

    private TokenResponse checkError(TokenResponse tokenResponse) throws OpenIDException {
        if (tokenResponse.getError() != null) {
            throw new OpenIDException("Token request failed: " + tokenResponse.getError());
        }
        return tokenResponse;
    }

}
