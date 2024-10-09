package org.keycloak.examples;

import org.keycloak.Config;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.authentication.ClientAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.*;

public class SpiffeClientAuthenticatorProviderFactory implements ClientAuthenticatorFactory {

    AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED};


    @Override
    public ClientAuthenticator create() {
        return new SpiffeClientAuthenticatorProvider();
    }

    @Override
    public String getDisplayType() {
        return "spiffe-jwt-svid";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigPropertiesPerClient() {
        return List.of(
                new ProviderConfigProperty("sub", "Subject", "Subject", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty("aud", "Audience", "Audience", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty("jwks", "JWKs", "JWKs", ProviderConfigProperty.STRING_TYPE, null));
    }

    @Override
    public Map<String, Object> getAdapterConfiguration(ClientModel clientModel) {
        return Map.of();
    }

    @Override
    public Set<String> getProtocolAuthenticatorMethods(String loginProtocol) {
        if (loginProtocol.equals(OIDCLoginProtocol.LOGIN_PROTOCOL)) {
            Set<String> results = new HashSet<>();
            results.add("jwt-svid");
            return results;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public String getHelpText() {
        return "JWT-SVID authenticator";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return List.of(
                new ProviderConfigProperty("sub", "Subject", "Subject", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty("aud", "Audience", "Audience", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty("jwks", "JWKs", "JWKs", ProviderConfigProperty.STRING_TYPE, null));
    }

    @Override
    public ClientAuthenticator create(KeycloakSession keycloakSession) {
        return new SpiffeClientAuthenticatorProvider();
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "spiffe-jwt-svid";
    }
}
