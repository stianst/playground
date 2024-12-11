package org.keycloak.examples;

import java.net.URI;

class SpiffeID {

    private String domain;
    private String workloadId;

    static SpiffeID parse(String spiffeId) {
        URI uri = URI.create(spiffeId);
        if ("spiffe".equals(uri.getScheme())) {
            return new SpiffeID(uri.getHost(), uri.getPath().substring(1));
        } else {
            return null;
        }
    }

    SpiffeID(String domain, String workloadId) {
        this.domain = domain;
        this.workloadId = workloadId;
    }

    public String getDomain() {
        return domain;
    }

    public String getWorkloadId() {
        return workloadId;
    }

    @Override
    public String toString() {
        return "spiffe://" + domain + "/" + workloadId;
    }
}
