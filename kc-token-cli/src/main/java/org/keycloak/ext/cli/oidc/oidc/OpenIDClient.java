package org.keycloak.ext.cli.oidc.oidc;

import org.keycloak.ext.cli.oidc.Output;
import org.keycloak.ext.cli.oidc.config.Context;
import org.keycloak.ext.cli.oidc.http.Http;
import org.keycloak.ext.cli.oidc.http.MimeType;
import org.keycloak.ext.cli.oidc.oidc.representations.DeviceAuthorizationResponse;
import org.keycloak.ext.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.ext.cli.oidc.oidc.representations.WellKnown;

import java.util.concurrent.TimeUnit;

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
        switch (configuration.getFlow()) {
            case RESOURCE_OWNER:
                return resourceOwner();
            case DEVICE:
                return device();
            default:
                throw new RuntimeException("Unknown flow");
        }
    }

    public TokenResponse refresh(String refreshToken) {
        TokenResponse tokenResponse = clientRequest(wellKnown.getTokenEndpoint())
                .accept(MimeType.JSON)
                .contentType(MimeType.FORM)
                .body("grant_type", "refresh_token")
                .body("refresh_token", refreshToken)
                .body("scope", "openid")
                .asObject(TokenResponse.class);
        return checkError(tokenResponse);
    }

    private TokenResponse resourceOwner() {
        TokenResponse tokenResponse = clientRequest(wellKnown.getTokenEndpoint())
                .accept(MimeType.JSON)
                .contentType(MimeType.FORM)
                .body("grant_type", "password")
                .body("scope", "openid")
                .body("username", configuration.getUsername())
                .body("password", configuration.getUserPassword())
                .asObject(TokenResponse.class);
        return checkError(tokenResponse);
    }

    private TokenResponse device() {
        DeviceAuthorizationResponse deviceAuthorizationResponse = clientRequest(wellKnown.getDeviceAuthorizationEndpoint())
                .accept(MimeType.JSON)
                .contentType(MimeType.FORM)
                .body("scope", "openid")
                .asObject(DeviceAuthorizationResponse.class);

        if (deviceAuthorizationResponse.getVerificationUriComplete() != null) {
            Output.println("Open the following URL to complete login:");
            Output.println(deviceAuthorizationResponse.getVerificationUriComplete());
        } else {
            Output.println("Open the following URL to complete login:");
            Output.println(deviceAuthorizationResponse.getVerificationUri());
            Output.println("");
            Output.println("Enter the code:");
            Output.println(deviceAuthorizationResponse.getUserCode());
        }

        long interval = TimeUnit.SECONDS.toMillis(deviceAuthorizationResponse.getInterval() != null ? deviceAuthorizationResponse.getInterval() : 5);

        long stop = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        while (System.currentTimeMillis() < stop) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }

            TokenResponse tokenResponse = clientRequest(wellKnown.getTokenEndpoint())
                    .contentType(MimeType.FORM)
                    .body("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                    .body("device_code", deviceAuthorizationResponse.getDeviceCode())
                    .body("scope", "openid")
                    .asObject(TokenResponse.class);

            if (tokenResponse.getError() == null || !tokenResponse.getError().equals("authorization_pending")) {
                return checkError(tokenResponse);
            }
        }

        throw new RuntimeException("Device authorization request timed out");
    }

    private Http clientRequest(String endpoint) {
        Http http = Http.create(endpoint);
        if (configuration.getClientSecret() != null) {
            http.authorization(configuration.getClientId(), configuration.getClientSecret());
        } else {
            http.body("client_id", configuration.getClientId());
        }
        return http;
    }

    private TokenResponse checkError(TokenResponse tokenResponse) {
        if (tokenResponse.getError() != null) {
            throw new RuntimeException("Request failed " + tokenResponse.getError());
        }
        return tokenResponse;
    }

}
