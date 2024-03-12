package org.keycloak.ext.theme;

import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DummyUserModel implements UserModel {
    @Override
    public String getId() {
        return "dummy";
    }

    @Override
    public String getUsername() {
        return "Dummy Username";
    }

    @Override
    public void setUsername(String s) {

    }

    @Override
    public Long getCreatedTimestamp() {
        return null;
    }

    @Override
    public void setCreatedTimestamp(Long aLong) {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean b) {

    }

    @Override
    public void setSingleAttribute(String s, String s1) {

    }

    @Override
    public void setAttribute(String s, List<String> list) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getFirstAttribute(String s) {
        return null;
    }

    @Override
    public Stream<String> getAttributeStream(String s) {
        return null;
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return null;
    }

    @Override
    public Stream<String> getRequiredActionsStream() {
        return new LinkedList<String>().stream();
    }

    @Override
    public void addRequiredAction(String s) {

    }

    @Override
    public void removeRequiredAction(String s) {

    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public void setFirstName(String s) {

    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public void setLastName(String s) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String s) {

    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @Override
    public void setEmailVerified(boolean b) {

    }

    @Override
    public Stream<GroupModel> getGroupsStream() {
        return null;
    }

    @Override
    public void joinGroup(GroupModel groupModel) {

    }

    @Override
    public void leaveGroup(GroupModel groupModel) {

    }

    @Override
    public boolean isMemberOf(GroupModel groupModel) {
        return false;
    }

    @Override
    public String getFederationLink() {
        return null;
    }

    @Override
    public void setFederationLink(String s) {

    }

    @Override
    public String getServiceAccountClientLink() {
        return null;
    }

    @Override
    public void setServiceAccountClientLink(String s) {

    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new DummySubjectCredentialManager();
    }

    @Override
    public Stream<RoleModel> getRealmRoleMappingsStream() {
        return null;
    }

    @Override
    public Stream<RoleModel> getClientRoleMappingsStream(ClientModel clientModel) {
        return null;
    }

    @Override
    public boolean hasRole(RoleModel roleModel) {
        return false;
    }

    @Override
    public void grantRole(RoleModel roleModel) {

    }

    @Override
    public Stream<RoleModel> getRoleMappingsStream() {
        return null;
    }

    @Override
    public void deleteRoleMapping(RoleModel roleModel) {

    }
}
