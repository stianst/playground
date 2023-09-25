package org.keycloak.cli.oidc.oidc.exceptions;

import java.io.IOException;

public class TokenRequestFailure extends OpenIDException {

    public TokenRequestFailure(IOException exception) {
        super("Token request failed", exception);
    }

}
