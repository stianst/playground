package org.keycloak.hostname.debug;

public class Constants {

    public static final String[] RELEVANT_HEADERS = new String[] {
            "Host",
            "Forwarded",
            "X-Forwarded-Host",
            "X-Forwarded-Proto",
            "X-Forwarded-Port",
            "X-Forwarded-For"
    };

    public static final String[] RELEVANT_OPTIONS = {
            "hostname",
            "hostname-url",
            "hostname-admin",
            "hostname-admin-url",
            "hostname-strict",
            "hostname-strict-backchannel",
            "hostname-strict-https",
            "hostname-path",
            "hostname-port",
            "proxy",
            "http-enabled",
            "http-relative-path",
            "http-port",
            "https-port"
    };

}
