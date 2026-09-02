package com.example.keycloak.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.model.policy.PolicySet;

public class DefaultCedarProvider implements CedarProvider {

    private final AuthorizationEngine engine;
    private final PolicySet policySet;

    public DefaultCedarProvider(AuthorizationEngine engine, PolicySet policySet) {
        this.engine = engine;
        this.policySet = policySet;
    }

    @Override
    public AuthorizationEngine getEngine() {
        return engine;
    }

    public PolicySet getPolicySet() {
        return policySet;
    }

    @Override
    public <T extends CedarRequest<?>> T request(Class<T> requestType) {
        try {
            T request = requestType.getConstructor().newInstance();
            request.provider(this);
            return request;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
    }
}
