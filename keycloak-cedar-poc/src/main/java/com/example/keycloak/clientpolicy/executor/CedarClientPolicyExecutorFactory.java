package com.example.keycloak.clientpolicy.executor;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.clientpolicy.executor.ClientPolicyExecutorProvider;
import org.keycloak.services.clientpolicy.executor.ClientPolicyExecutorProviderFactory;

import java.util.Collections;
import java.util.List;

public class CedarClientPolicyExecutorFactory implements ClientPolicyExecutorProviderFactory {

    public static final String PROVIDER_ID = "cedar-policies";

    @Override
    public ClientPolicyExecutorProvider create(KeycloakSession session) {
        return new CedarClientPolicyExecutor(session);
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "Invokes Cedar policies";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }
}
