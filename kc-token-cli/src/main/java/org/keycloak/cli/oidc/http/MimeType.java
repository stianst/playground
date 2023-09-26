package org.keycloak.cli.oidc.http;

public enum MimeType {

    FORM("application/x-www-form-urlencoded"),
    JSON("application/json"),
    HTML("text/html"),
    X_ICON("image/x-icon");

    private String mimeType;

    MimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return mimeType;
    }
}
