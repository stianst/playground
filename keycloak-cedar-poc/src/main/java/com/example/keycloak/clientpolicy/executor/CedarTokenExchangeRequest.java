package com.example.keycloak.clientpolicy.executor;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.model.AuthorizationRequest;
import com.cedarpolicy.model.AuthorizationResponse;
import com.cedarpolicy.model.entity.Entity;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.policy.PolicySet;
import com.cedarpolicy.value.*;
import org.jboss.logging.Logger;

import java.util.*;

public class CedarTokenExchangeRequest {

    private static final Logger LOG = Logger.getLogger(CedarTokenExchangeRequest.class);

    private final Cedar cedar;
    private final Entity principal;
    private final Entity action = createEntity("Action", "token-exchange");
    private final Entity resource;
    private final Map<String, Value> context = new HashMap<>();

    private CedarTokenExchangeRequest(Cedar cedar, String issuer, String clientId) {
        this.cedar = cedar;
        principal = createEntity("Client", clientId);
        resource = createEntity("Issuer", issuer);
    }

    public static CedarTokenExchangeRequest create(Cedar cedar, String issuer, String clientId) {
        return new CedarTokenExchangeRequest(cedar, issuer, clientId);
    }

    public boolean evalute() {
        AuthorizationEngine engine = cedar.getEngine();
        PolicySet policySet = cedar.getPolicySet();

        if (policySet == null) {
            LOG.warn("No cedar policy available");
            return true;
        }

        AuthorizationRequest request = new AuthorizationRequest(principal, action, resource, context);
        AuthorizationResponse authorized = null;
        try {
            authorized = engine.isAuthorized(request, policySet, Collections.emptySet());
        } catch (AuthException e) {
            LOG.warn("Failed to evaluate request", e);
            return false;
        }
        if (authorized.success.isPresent()) {
            if (authorized.success.get().isAllowed()) {
                LOG.infov("Permitted");
                return true;
            } else {
                LOG.infov("Not allowed");
                return false;
            }
        } else {
            LOG.infov("Failed");
            return false;
        }
    }

    public CedarTokenExchangeRequest withAudience(List<String> audience) {
        if (audience != null && !audience.isEmpty()) {
            context.put("audience", toValue(audience));
        }
        return this;
    }

    public CedarTokenExchangeRequest withScope(String scope) {
        if (scope != null) {
            context.put("scope", toValue(Arrays.asList(scope.split(" "))));
        }
        return this;
    }

    public CedarTokenExchangeRequest withRequestedTokenType(String requestedTokenType) {
        if (requestedTokenType != null) {
            context.put("requestedTokenType", toValue(requestedTokenType));
        }
        return this;
    }

    public CedarTokenExchangeRequest withSubjectToken(String subjectToken, String subjectTokenType) {
        context.put("subjectToken", toValue(subjectToken != null));
        if (subjectTokenType != null) {
            context.put("subjectTokenType", toValue(subjectTokenType));
        }
        return this;
    }

    public CedarTokenExchangeRequest withActorToken(String actorToken, String actorTokenType) {
        context.put("actorToken", toValue(actorToken != null));
        if (actorTokenType != null) {
            context.put("actorTokenType", toValue(actorTokenType));
        }
        return this;
    }

    private Entity createEntity(String typeName, String uuid) {
        EntityTypeName entityTypeName = EntityTypeName.parse(typeName).orElseThrow();
        EntityUID entityUID = new EntityUID(entityTypeName, uuid);
        return new Entity(entityUID);
    }

    private static Value toValue(List<String> list) {
        CedarList value = new CedarList();
        list.forEach(v -> value.add(toValue(v)));
        return value;
    }

    private static Value toValue(String value) {
        return new PrimString(value);
    }

    private static Value toValue(boolean value) {
        return new PrimBool(value);
    }

}
