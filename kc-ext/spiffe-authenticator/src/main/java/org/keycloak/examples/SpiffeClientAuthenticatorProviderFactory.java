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

    private static final String ID = "spiffe-jwt-svid";

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED};

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public ClientAuthenticator create() {
        return new SpiffeClientAuthenticatorProvider();
    }

    @Override
    public String getDisplayType() {
        return "SPIFFE JWT-SVID authenticator";
    }

    @Override
    public String getHelpText() {
        return "SPIFFE JWT-SVID authenticator";
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
        return SpiffeConfig.CONFIG;
    }

    @Override
    public Map<String, Object> getAdapterConfiguration(ClientModel clientModel) {
        return Collections.emptyMap();
    }

    @Override
    public Set<String> getProtocolAuthenticatorMethods(String loginProtocol) {
        if (loginProtocol.equals(OIDCLoginProtocol.LOGIN_PROTOCOL)) {
            return Collections.singleton(ID);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return SpiffeConfig.CONFIG;
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

}
