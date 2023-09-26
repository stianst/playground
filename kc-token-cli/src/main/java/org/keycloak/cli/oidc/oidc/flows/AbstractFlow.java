package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.client.Http;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

public abstract class AbstractFlow {

    protected Context context;
    protected WellKnown wellKnown;

    public AbstractFlow(Context context, WellKnown wellKnown) {
        this.context = context;
        this.wellKnown = wellKnown;
    }

    public abstract TokenResponse execute() throws OpenIDException;

    protected Http clientRequest(String endpoint) {
        Http http = Http.create(endpoint).userAgent("kc-oidc/1.0");
        if (context.getClientSecret() != null) {
            http.authorization(context.getClientId(), context.getClientSecret());
        } else {
            http.body("client_id", context.getClientId());
        }
        return http;
    }

}
