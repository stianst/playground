package io.stianst;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class MyCustomResourceProvider implements RealmResourceProvider {
    private KeycloakSession session;

    public MyCustomResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() throws IOException {
        System.out.println("Hello!");
        return "Hello " + SimpleHttp.doGet("http://localhost:8080/realms/master/custom/name", session).asString();
    }

    @GET
    @Path("/name")
    @Produces(MediaType.TEXT_PLAIN)
    public String getName() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Bob!");
        return "Bob";
    }

    @Override
    public void close() {

    }
}
