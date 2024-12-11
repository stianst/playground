package org.keycloak.examples;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.authentication.ClientAuthenticationFlowContext;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.authentication.authenticators.client.ClientAuthUtil;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.models.ClientModel;
import org.keycloak.representations.JsonWebToken;

import java.net.URI;

public class SpiffeClientAuthenticatorProvider implements ClientAuthenticator {

    private static final Logger LOGGER = Logger.getLogger(SpiffeClientAuthenticatorProvider.class);

    @Override
    public void authenticateClient(ClientAuthenticationFlowContext context) {
        try {
            SpiffeConfig config = SpiffeConfig.parse(context.getAuthenticatorConfig().getConfig());

            SpiffeAuthenticator authenticator = SpiffeAuthenticator.parse(context);
            if (authenticator != null) {

            }
            MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
            String clientAssertionType = params.getFirst(SpiffeConstants.ASSERTION_TYPE);

            LOGGER.info("client_assertion_type: " + clientAssertionType);

            if (clientAssertionType != null && clientAssertionType.equals("spiffe-jwt-svid")) {
                String clientAssertion = params.getFirst("client_assertion");
                if (clientAssertion != null) {
                    JWSInput jwsInput = new JWSInput(clientAssertion);

                    LOGGER.info("Algorithm: " + jwsInput.getHeader().getAlgorithm());


                    JsonWebToken jwt = jwsInput.readJsonContent(JsonWebToken.class);
                    String subject = jwt.getSubject();
                    URI uri = new URI(subject);
                    String scheme = uri.getScheme();
                    String trustDomain = uri.getHost();
                    String workloadIdentifier = uri.getPath().substring(1);

                    LOGGER.info("scheme: " + scheme);
                    LOGGER.info("trust domain: " + trustDomain);
                    LOGGER.info("workload: " + workloadIdentifier);

                    ClientModel client = context.getRealm().getClientByClientId(workloadIdentifier);

                    LOGGER.info("client: " + client);

                    context.setClient(client);

                    context.success();
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to process client authentication", e);
            Response response = ClientAuthUtil.errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "invalid_client", e.getMessage());
            context.challenge(response);
        }
    }

    @Override
    public void close() {

    }
}
