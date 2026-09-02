package com.example.keycloak.clientpolicy.executor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.clientpolicy.executor.ClientPolicyExecutorProvider;
import org.keycloak.services.clientpolicy.executor.ClientPolicyExecutorProviderFactory;

public class CedarClientPolicyExecutorFactory implements ClientPolicyExecutorProviderFactory {

    public static final String PROVIDER_ID = "cedar-policies";

    private Cedar cedar;

    private File dataDir;

    @Override
    public ClientPolicyExecutorProvider create(KeycloakSession session) {
        return new CedarClientPolicyExecutor(session, cedar);
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        try {
            File dataDir = new File(System.getProperty("kc.io.tmpdir")).getParentFile().getCanonicalFile();
            cedar = new Cedar(new File(dataDir, "client-policies.cedar"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
