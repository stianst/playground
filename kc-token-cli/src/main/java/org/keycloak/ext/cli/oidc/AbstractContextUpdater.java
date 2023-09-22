package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.Context;
import org.keycloak.ext.cli.oidc.oidc.OpenIDFlow;

public class AbstractContextUpdater extends CommonOptions {

    public void update(Context context) {
        if (iss != null) {
            context.setIssuer(iss);
        }
        if (flow != null) {
            if (flow.equals("null")) {
                context.setFlow(null);
            } else {
                context.setFlow(OpenIDFlow.valueOf(flow.replace('-', '_').toUpperCase()));
            }
        }
        if (clientId != null) {
            context.setClientId(convertToNull(clientId));
        }
        if (clientSecret != null) {
            context.setClientSecret(convertToNull(clientSecret));
        }
        if (user != null) {
            context.setUsername(convertToNull(user));
        }
        if (password != null) {
            context.setUserPassword(convertToNull(password));
        }
        if (storeTokens != null) {
            String v = convertToNull(storeTokens);
            if (v != null) {
                context.setStoreTokens(Boolean.valueOf(v));
            } else {
                context.setStoreTokens(null);
            }
        }
    }

    private String convertToNull(String s) {
        return s != null && s.equals("null") ? null : s;
    }

}
