package org.keycloak.examples;

import org.jboss.logging.Logger;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;
import java.util.Map;

class SpiffeConfig {

    private static final Logger LOGGER = Logger.getLogger(SpiffeConfig.class);

    private static final String DOMAIN_KEY = "domain";
    private static final String AUD_KEY = "aud";
    private static final String JWKS_KEY = "jwks";

    static final List<ProviderConfigProperty> CONFIG = List.of(
            new ProviderConfigProperty(DOMAIN_KEY, "Domain", "Domain", ProviderConfigProperty.STRING_TYPE, null),
            new ProviderConfigProperty(AUD_KEY, "Audience", "Audience", ProviderConfigProperty.STRING_TYPE, null),
            new ProviderConfigProperty(JWKS_KEY, "JWKs", "JWKs", ProviderConfigProperty.STRING_TYPE, null));

    private final String domain;
    private final String aud;
    private final String jwks;

    static SpiffeConfig parse(Map<String, String> config) {
        String domain = config.get(DOMAIN_KEY);
        String aud = config.get(AUD_KEY);
        String jwks = config.get(JWKS_KEY);

        LOGGER.infov("SPIFFE config: aud={0}, domain={1}, jwks={2}", aud, domain, jwks);

        return new SpiffeConfig(domain, jwks, aud);
    }

    public SpiffeConfig(String domain, String jwks, String aud) {
        this.aud = aud;
        this.domain = domain;
        this.jwks = jwks;
    }

    public String getDomain() {
        return domain;
    }

    public String getJwks() {
        return jwks;
    }

    public String getAud() {
        return aud;
    }

}
