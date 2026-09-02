package com.example.keycloak.cedar;

import com.cedarpolicy.model.policy.PolicySet;

import java.io.File;

public class FilePolicySet {

    private final File policyFile;
    private long lastModified = -1;

    private PolicySet policySet;

    public FilePolicySet(File policyFile) {
        this.policyFile = policyFile;
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
