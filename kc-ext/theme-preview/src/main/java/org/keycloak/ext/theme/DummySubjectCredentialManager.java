package org.keycloak.ext.theme;

import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.SubjectCredentialManager;

import java.util.List;
import java.util.stream.Stream;

public class DummySubjectCredentialManager implements SubjectCredentialManager {
    @Override
    public boolean isValid(List<CredentialInput> list) {
        return false;
    }

    @Override
    public boolean updateCredential(CredentialInput credentialInput) {
        return false;
    }

    @Override
    public void updateStoredCredential(CredentialModel credentialModel) {

    }

    @Override
    public CredentialModel createStoredCredential(CredentialModel credentialModel) {
        return null;
    }

    @Override
    public boolean removeStoredCredentialById(String s) {
        return false;
    }

    @Override
    public CredentialModel getStoredCredentialById(String s) {
        return null;
    }

    @Override
    public Stream<CredentialModel> getStoredCredentialsStream() {
        return null;
    }

    @Override
    public Stream<CredentialModel> getStoredCredentialsByTypeStream(String s) {
        return null;
    }

    @Override
    public CredentialModel getStoredCredentialByNameAndType(String s, String s1) {
        return null;
    }

    @Override
    public boolean moveStoredCredentialTo(String s, String s1) {
        return false;
    }

    @Override
    public void updateCredentialLabel(String s, String s1) {

    }

    @Override
    public void disableCredentialType(String s) {

    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream() {
        return null;
    }

    @Override
    public boolean isConfiguredFor(String s) {
        return false;
    }

    @Override
    public boolean isConfiguredLocally(String s) {
        return false;
    }

    @Override
    public Stream<String> getConfiguredUserStorageCredentialTypesStream() {
        return null;
    }

    @Override
    public CredentialModel createCredentialThroughProvider(CredentialModel credentialModel) {
        return null;
    }
}
