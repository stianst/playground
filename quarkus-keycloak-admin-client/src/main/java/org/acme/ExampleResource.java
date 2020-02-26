package org.acme;

import org.jboss.logging.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    private static final Logger LOG = Logger.getLogger(ExampleResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        LOG.info("Hello invoked");

        Keycloak keycloak = KeycloakBuilder.builder().serverUrl("http://localhost:8080/auth").realm("master").clientId("admin-cli").username("admin").password("admin").build();
        return keycloak.realm("master").toRepresentation().getRealm();
    }
}