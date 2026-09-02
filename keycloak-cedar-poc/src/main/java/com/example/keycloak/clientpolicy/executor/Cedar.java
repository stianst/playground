package com.example.keycloak.clientpolicy.executor;

import com.cedarpolicy.AuthorizationEngine;
import com.cedarpolicy.BasicAuthorizationEngine;
import com.cedarpolicy.model.exception.InternalException;
import com.cedarpolicy.model.policy.PolicySet;

import java.io.File;
import java.io.IOException;

public class Cedar {

    private final AuthorizationEngine engine = new BasicAuthorizationEngine();
    private final File policyFile;

    private PolicySet policySet;
    private long lastModified = -1;

    public Cedar(File policyFile) throws IOException, InternalException {
        this.policyFile = policyFile;
    }

    public AuthorizationEngine getEngine() {
        return engine;
    }

    public PolicySet getPolicySet() {
        if (!policyFile.isFile()) {
            return null;
        }

        long updatedLastModified = policyFile.lastModified();

        if (policySet == null || lastModified != updatedLastModified) {
            lastModified = updatedLastModified;
            try {
                this.policySet = PolicySet.parsePolicies(policyFile.toPath());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return policySet;
    }

}
