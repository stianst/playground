package org.keycloak.cli.oidc.oidc;

import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.Http;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.flows.AbstractFlow;
import org.keycloak.cli.oidc.oidc.flows.ClientCredentialFlow;
import org.keycloak.cli.oidc.oidc.flows.DeviceFlow;
import org.keycloak.cli.oidc.oidc.flows.RefreshFlow;
import org.keycloak.cli.oidc.oidc.flows.ResourceOwnerFlow;
import org.keycloak.cli.oidc.oidc.representations.JWT;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OpenIDClient {

    private ConfigHandler configHandler;
    private Context context;
    private WellKnown wellKnown;

    public OpenIDClient(ConfigHandler configHandler, Context context) throws OpenIDException {
        this.context = context;
        this.configHandler = configHandler;

        try {
            wellKnown = Http.create(context.getIssuer() + "/.well-known/openid-configuration")
                    .userAgent("kc-oidc/1.0")
                    .accept(MimeType.JSON)
                    .asObject(WellKnown.class);
        } catch (IOException e) {
            throw new OpenIDException("Failed to retrieve well-known endpoint", e);
        }
    }

    public static OpenIDClient create(ConfigHandler configHandler, Context configuration) throws OpenIDException {
        return new OpenIDClient(configHandler, configuration);
    }

    public String getToken(String tokenType, boolean offline) throws OpenIDException, ConfigException {
        boolean refresh = tokenType.equals("refresh");
        String savedToken = getSaved(context, tokenType);

        if (isValid(savedToken)) {
            return savedToken;
        } else if (offline) {
            throw new RuntimeException("Token expired");
        }

        TokenResponse tokenResponse = null;
        if (!refresh) {
            String refreshToken = context.getRefreshToken();
            if (isValid(refreshToken)) {
                try {
                    tokenResponse = refreshRequest(refreshToken);
                } catch (OpenIDException e) {
                }
            }
        }

        if (tokenResponse == null) {
            tokenResponse = tokenRequest();
        }

        if (context.isStoreTokens() == null || context.isStoreTokens()) {
            context.setRefreshToken(tokenResponse.getRefreshToken());
            context.setIdToken(tokenResponse.getIdToken());
            context.setAccessToken(tokenResponse.getAccessToken());
            configHandler.save();
        }

        return getToken(tokenResponse, tokenType);
    }

    private boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        JWT jwt = TokenParser.parse(token).getJWT();
        long exp = TimeUnit.SECONDS.toMillis(Long.valueOf(jwt.getExp()));
        return exp > System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
    }

    private String getSaved(Context context, String tokenType) {
        switch (tokenType) {
            case "id":
                return context.getIdToken();
            case "access":
                return context.getAccessToken();
            case "refresh":
                return context.getRefreshToken();
        }
        throw new RuntimeException("Unknown token type");
    }

    private String getToken(TokenResponse tokenResponse, String tokenType) {
        switch (tokenType) {
            case "id":
                return tokenResponse.getIdToken();
            case "access":
                return tokenResponse.getAccessToken();
            case "refresh":
                return tokenResponse.getRefreshToken();
        }
        throw new RuntimeException("Unknown token type");
    }

    public TokenResponse tokenRequest() throws OpenIDException {
        AbstractFlow flow;
        switch (context.getFlow()) {
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
