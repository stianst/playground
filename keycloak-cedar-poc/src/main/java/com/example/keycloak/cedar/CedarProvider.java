package com.example.keycloak.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.model.policy.PolicySet;
import org.keycloak.provider.Provider;

public interface CedarProvider extends Provider {

    <T extends CedarRequest<?>> T request(Class<T> requestType);

    AuthorizationEngine getEngine();

    PolicySet getPolicySet();

}
