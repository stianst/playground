package com.example.keycloak.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.model.AuthorizationRequest;
import com.cedarpolicy.model.AuthorizationResponse;
import com.cedarpolicy.model.entity.Entity;
import com.cedarpolicy.model.exception.AuthException;
import com.cedarpolicy.model.policy.PolicySet;
import com.cedarpolicy.value.*;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CedarRequest<T> {

    private static final Logger LOG = Logger.getLogger(CedarRequest.class);

    private CedarProvider cedar;

    private Entity principal;
    private Entity resource;
    private Entity action;

    private final Map<String, Value> context = new HashMap<>();

    public T provider(CedarProvider cedar) {
        this.cedar = cedar;
        return asReturn();
    }

    public T principal(String typeName, String uuid) {
        principal = createEntity(typeName, uuid);
        return asReturn();
    }

    public T resource(String typeName, String uuid) {
        resource = createEntity(typeName, uuid);
        return asReturn();
    }

    public T action(String uuid) {
        action = createEntity("Action", "token-exchange");
        return asReturn();
    }

    public T context(String key, String value) {
        if (value != null) {
            context.put(key, toValue(value));
        }
        return asReturn();
    }

    public T context(String key, List<String> value) {
        if (value != null && !value.isEmpty()) {
            context.put(key, toValue(value));
        }
        return asReturn();
    }

    public T context(String key, boolean value) {
        context.put(key, toValue(value));
        return asReturn();
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

    private T asReturn() {
        T t = (T) this;
        return t;
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
