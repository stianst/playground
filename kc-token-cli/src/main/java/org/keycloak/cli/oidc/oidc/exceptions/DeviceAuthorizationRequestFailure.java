package org.keycloak.cli.oidc.oidc.exceptions;

import java.io.IOException;

public class DeviceAuthorizationRequestFailure extends OpenIDException {

    public DeviceAuthorizationRequestFailure(IOException e) {
        super("Device authorization request failed", e);
    }

}
