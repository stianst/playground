package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.Output;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.oidc.exceptions.DeviceAuthorizationRequestFailure;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.exceptions.TokenRequestFailure;
import org.keycloak.cli.oidc.oidc.representations.DeviceAuthorizationResponse;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DeviceFlow extends AbstractFlow {
    public DeviceFlow(Context configuration, WellKnown wellKnown) {
        super(configuration, wellKnown);
    }

    public TokenResponse execute() throws OpenIDException {
        DeviceAuthorizationResponse deviceAuthorizationResponse = null;
        try {
            deviceAuthorizationResponse = clientRequest(wellKnown.getDeviceAuthorizationEndpoint())
                    .accept(MimeType.JSON)
                    .contentType(MimeType.FORM)
                    .body("scope", "openid")
                    .asObject(DeviceAuthorizationResponse.class);
        } catch (IOException e) {
            throw new DeviceAuthorizationRequestFailure(e);
        }

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

            TokenResponse tokenResponse = null;
            try {
                tokenResponse = clientRequest(wellKnown.getTokenEndpoint())
                        .contentType(MimeType.FORM)
                        .body("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                        .body("device_code", deviceAuthorizationResponse.getDeviceCode())
                        .body("scope", "openid")
                        .asObject(TokenResponse.class);
            } catch (IOException e) {
                throw new TokenRequestFailure(e);
            }

            if (tokenResponse.getError() == null || !tokenResponse.getError().equals("authorization_pending")) {
                return tokenResponse;
            }
        }

        throw new RuntimeException("Device authorization request timed out");
    }
}
