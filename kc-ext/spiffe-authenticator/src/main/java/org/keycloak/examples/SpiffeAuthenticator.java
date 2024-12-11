package org.keycloak.examples;

import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;
import org.keycloak.authentication.ClientAuthenticationFlowContext;
import org.keycloak.jose.jwk.JSONWebKeySet;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.jose.jws.Algorithm;
import org.keycloak.jose.jws.JWSHeader;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.JsonWebToken;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;
import java.net.URL;

class SpiffeAuthenticator {

    private static final Logger LOGGER = Logger.getLogger(SpiffeAuthenticator.class);

    static final String ASSERTION_TYPE = "spiffe-jwt-svid";
    static final String ASSERTION_TYPE_KEY = "client_assertion_type";
    static final String ASSERTION_KEY = "client_assertion";

    private final String clientAssertion;
    private final SpiffeConfig config;

    public SpiffeAuthenticator(String clientAssertion, SpiffeConfig config) {
        this.clientAssertion = clientAssertion;
        this.config = config;
    }

    static SpiffeAuthenticator parse(ClientAuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        String clientAssertionType = params.getFirst(ASSERTION_TYPE_KEY);
        String clientAssertion = params.getFirst(ASSERTION_KEY);
        if (ASSERTION_TYPE.equals(clientAssertionType)) {
            SpiffeConfig config = SpiffeConfig.parse(context.getAuthenticatorConfig().getConfig());
            return new SpiffeAuthenticator(clientAssertion, config);
        } else {
            return null;
        }
    }

    public boolean verify() throws JWSInputException, IOException {
        JWSInput jws = new JWSInput(clientAssertion);
        JWSHeader header = jws.getHeader();
        JsonWebToken token = jws.readJsonContent(JsonWebToken.class);

        String keyId = header.getKeyId();
        Algorithm algorithm = header.getAlgorithm();

        SpiffeID subjectId = SpiffeID.parse(token.getSubject());

        if (!config.getDomain().equals(subjectId.getDomain())) {
            LOGGER.warn("Invalid domain");
            return false;
        }

        SpiffeID expectedAudience = new SpiffeID(config.getDomain(), config.getAud());

        if (!checkAudience(expectedAudience.toString(), token.getAudience())) {
            LOGGER.warn("Invalid audience");
            return false;
        }

        if (!checkSignature(header)) {
            LOGGER.warn("Invalid signature");
            return false;
        }


        System.out.println(keyId);
        System.out.println(algorithm);
        System.out.println(token.getSubject());
        System.out.println(String.join(", ", token.getAudience()));

        return true;
    }

    private boolean checkSignature(JWSHeader header) throws IOException {
        JSONWebKeySet jwks = JsonSerialization.readValue(new URL(config.getJwks()).openStream(), JSONWebKeySet.class);

        JWK jwk = null;
        for (JWK j : jwks.getKeys()) {
            if (j.getKeyId().equals(header.getKeyId())) {
                jwk = j;
                break;
            }
        }

        System.out.println("key = " + jwk);

        return true;
    }

    private boolean checkAudience(String expectedAudience, String[] audience) {
        for (String a : audience) {
            if (expectedAudience.equals(a)) {
                return true;
            }
        }
        return false;
    }

}
