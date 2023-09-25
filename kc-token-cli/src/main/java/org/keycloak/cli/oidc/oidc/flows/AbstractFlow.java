package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.Http;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

public abstract class AbstractFlow {

    protected Context configuration;
    protected WellKnown wellKnown;

    public AbstractFlow(Context configuration, WellKnown wellKnown) {
        this.configuration = configuration;
        this.wellKnown = wellKnown;
    }

    public abstract TokenResponse execute() throws OpenIDException;

    protected Http clientRequest(String endpoint) {
        Http http = Http.create(endpoint);
        if (configuration.getClientSecret() != null) {
            http.authorization(configuration.getClientId(), configuration.getClientSecret());
        } else {
            http.body("client_id", configuration.getClientId());
        }
        return http;
    }

}
