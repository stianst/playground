package com.example.keycloak.clientpolicy.executor;

import com.example.keycloak.cedar.CedarRequest;

import java.util.Arrays;
import java.util.List;

public class CedarTokenExchangeRequest extends CedarRequest<CedarTokenExchangeRequest> {

    public CedarTokenExchangeRequest() {
        action("token-exchange");
    }

    public CedarTokenExchangeRequest client(String clientId) {
        return principal("Client", clientId);
    }

    public CedarTokenExchangeRequest issuer(String issuer) {
        return resource("Issuer", issuer);
    }

    public CedarTokenExchangeRequest audience(List<String> audience) {
        return context("audience", audience);
    }

    public CedarTokenExchangeRequest scope(String scope) {
        return context("scope", Arrays.asList(scope.split(" ")));
    }

    public CedarTokenExchangeRequest requestedTokenType(String requestedTokenType) {
        return context("requestedTokenType", requestedTokenType);
    }

    public CedarTokenExchangeRequest subjectToken(String subjectToken, String subjectTokenType) {
        return context("subjectToken", subjectToken != null).context("subjectTokenType", subjectTokenType);
    }

    public CedarTokenExchangeRequest actorToken(String actorToken, String actorTokenType) {
        return context("actorToken", actorToken != null).context("actorTokenType", actorTokenType);
    }

}
