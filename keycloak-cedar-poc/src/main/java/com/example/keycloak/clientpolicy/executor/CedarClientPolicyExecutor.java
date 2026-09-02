package com.example.keycloak.clientpolicy.executor;

import com.example.keycloak.cedar.CedarProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.TokenExchangeContext;
import org.keycloak.representations.idm.ClientPolicyExecutorConfigurationRepresentation;
import org.keycloak.services.Urls;
import org.keycloak.services.clientpolicy.ClientPolicyContext;
import org.keycloak.services.clientpolicy.ClientPolicyException;
import org.keycloak.services.clientpolicy.context.TokenExchangeRequestContext;
import org.keycloak.services.clientpolicy.executor.ClientPolicyExecutorProvider;
import org.keycloak.urls.UrlType;

public class CedarClientPolicyExecutor implements ClientPolicyExecutorProvider<ClientPolicyExecutorConfigurationRepresentation> {

    private final KeycloakSession session;

    public CedarClientPolicyExecutor(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public String getProviderId() {
        return CedarClientPolicyExecutorFactory.PROVIDER_ID;
    }

    @Override
    public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
        if (context instanceof TokenExchangeRequestContext tokenExchangeRequestContext) {
            CedarProvider cedar = session.getProvider(CedarProvider.class);

            TokenExchangeContext tokenExchangeContext = tokenExchangeRequestContext.getTokenExchangeContext();

            String issuer = Urls.realmIssuer(session.getContext().getUri(UrlType.FRONTEND).getBaseUri(), session.getContext().getRealm().getName());
            String clientId = tokenExchangeContext.getClient().getClientId();
            TokenExchangeContext.Params params = tokenExchangeContext.getParams();

            CedarTokenExchangeRequest exchangeRequest = cedar.request(CedarTokenExchangeRequest.class).issuer(issuer).client(clientId)
                    .audience(params.getAudience())
                    .actorToken(params.getActorToken(), params.getActorToken())
                    .requestedTokenType(params.getRequestedTokenType())
                    .subjectToken(params.getSubjectToken(), params.getSubjectTokenType())
                    .scope(params.getScope());

            boolean permitted = exchangeRequest.evalute();
            if (!permitted) {
                throw new ClientPolicyException("Not permitted by policy");
            }
        }
    }

}
