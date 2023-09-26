package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.User;
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

    private static final long DEFAULT_POOL_INTERVAL = TimeUnit.SECONDS.toMillis(5);
    private static final long MAX_WAIT = TimeUnit.MINUTES.toMillis(5);

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
            User.cli().print("Open the following URL to complete login:",
                    deviceAuthorizationResponse.getVerificationUriComplete());
        } else {
            User.cli().print("Open the following URL to complete login:",
                    deviceAuthorizationResponse.getVerificationUri(),
                    "",
                    "Enter the code:",
                    deviceAuthorizationResponse.getUserCode());
        }

        long interval = deviceAuthorizationResponse.getInterval() != null ? TimeUnit.SECONDS.toMillis(deviceAuthorizationResponse.getInterval()) : DEFAULT_POOL_INTERVAL;
        long stop = System.currentTimeMillis() + MAX_WAIT;

        while (System.currentTimeMillis() < stop) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }

            TokenResponse tokenResponse;
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
