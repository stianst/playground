package com.example.keycloak.cedar;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.BasicAuthorizationEngine;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.io.File;
import java.io.IOException;

public class DefaultCedarProviderFactory implements CedarProviderFactory {

    private String policyPath;
    private AuthorizationEngine engine;
    private FilePolicySet policySet;

    @Override
    public CedarProvider create(KeycloakSession session) {
        return new DefaultCedarProvider(engine, policySet.getPolicySet());
    }

    @Override
    public void init(Config.Scope config) {
        policyPath = config.get("policy", "policies.cedar");
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        File dataDir;
        try {
            System.out.println(System.getProperty("kc.io.tmpdir"));
            dataDir = new File(System.getProperty("kc.io.tmpdir")).getParentFile().getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File policyFile = new File(dataDir, policyPath);
        policySet = new FilePolicySet(policyFile);

        engine = new BasicAuthorizationEngine();
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "default";
    }
}
