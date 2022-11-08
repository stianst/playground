package org.keycloak.hostname.debug;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.quarkus.runtime.configuration.Configuration;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class HostnameDebugProviderFactory implements RealmResourceProviderFactory {

    private static final String[] RELEVANT_OPTIONS = {
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

    public static final String ID = "hostname-debug";

    private Map<String, String> config;

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new HostnameDebugProvider(session, config);
    }

    @Override
    public void init(Config.Scope config) {
        this.config = new LinkedHashMap<>();
        for (String key : RELEVANT_OPTIONS) {
            addOption(key);
        }
    }

    private void addOption(String key) {
        String rawValue = Configuration.getRawValue("kc." + key);
        if (rawValue != null && !rawValue.isEmpty()) {
            config.put(key, rawValue);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return ID;
    }
}
