package io.stianst.test;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class MyRealmResourceFactory implements RealmResourceProviderFactory, RealmResourceProvider {
    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return this;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "myresource";
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("")
    @Produces("text/plaiun")
    public String hello() {
        return "Hello world";
    }

}
