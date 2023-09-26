package org.keycloak.cli.oidc.oidc;

import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.client.Http;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.flows.AbstractFlow;
import org.keycloak.cli.oidc.oidc.flows.AuthorizationCodeFlow;
import org.keycloak.cli.oidc.oidc.flows.ClientCredentialFlow;
import org.keycloak.cli.oidc.oidc.flows.DeviceFlow;
import org.keycloak.cli.oidc.oidc.flows.RefreshFlow;
import org.keycloak.cli.oidc.oidc.flows.ResourceOwnerFlow;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;

public class OpenIDClient {

    public static void main(String[] args) throws OpenIDException {
        Context context = new Context();
        context.setIssuer("http://localhost:8080/realms/myrealm");
        context.setClientId("myclient");
        context.setClientSecret("secret");
        context.setFlow(OpenIDFlow.AUTHORIZATION_CODE);

        OpenIDClient client = new OpenIDClient(context);
        TokenResponse tokenResponse = client.tokenRequest();
        System.out.println("Error: " + tokenResponse.getError());
        System.out.println("Access token: " + tokenResponse.getAccessToken());
    }

    private Context context;
    private WellKnown wellKnown;

    public OpenIDClient(Context context) throws OpenIDException {
        this.context = context;

        try {
            wellKnown = Http.create(context.getIssuer() + "/.well-known/openid-configuration")
                    .userAgent("kc-oidc/1.0")
                    .accept(MimeType.JSON)
                    .asObject(WellKnown.class);
        } catch (IOException e) {
            throw new OpenIDException("Failed to retrieve well-known endpoint", e);
        }
    }

    public TokenResponse tokenRequest() throws OpenIDException {
        AbstractFlow flow;
        switch (context.getFlow()) {
            case AUTHORIZATION_CODE:
                flow = new AuthorizationCodeFlow(context, wellKnown);
                break;
            case RESOURCE_OWNER:
                flow = new ResourceOwnerFlow(context, wellKnown);
                break;
            case DEVICE:
                flow = new DeviceFlow(context, wellKnown);
                break;
            case CLIENT_CREDENTIAL:
                flow = new ClientCredentialFlow(context, wellKnown);
                break;
            default:
                throw new RuntimeException("Unknown flow");
        }

        TokenResponse tokenResponse = flow.execute();
        return checkError(tokenResponse);
    }

    public TokenResponse refreshRequest(String refreshToken) throws OpenIDException {
        TokenResponse tokenResponse = new RefreshFlow(refreshToken, context, wellKnown)
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
