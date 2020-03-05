package org.keycloak.example.basicauth;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.admin.client.token.TokenService;
import org.keycloak.representations.AccessTokenResponse;

import javax.ws.rs.core.Form;

import static org.keycloak.OAuth2Constants.CLIENT_ID;
import static org.keycloak.OAuth2Constants.GRANT_TYPE;

public class RestEasyTester {

    public String test() {
        ResteasyClient cb = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = cb.target("http://localhost:8180/auth");

        TokenService client = target.proxy(TokenService.class);

        Form form = new Form().param(GRANT_TYPE, "password");
        form.param("username", "admin")
            .param("password", "admin")
            .param(CLIENT_ID, "admin-cli");

        AccessTokenResponse response = client.grantToken("master", form.asMap());

        return response.getToken();
    }

}
