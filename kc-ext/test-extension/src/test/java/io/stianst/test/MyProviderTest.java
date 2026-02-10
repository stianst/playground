package io.stianst.test;

import jakarta.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.testframework.annotations.InjectKeycloakUrls;
import org.keycloak.testframework.annotations.InjectRealm;
import org.keycloak.testframework.annotations.InjectSimpleHttp;
import org.keycloak.testframework.annotations.KeycloakIntegrationTest;
import org.keycloak.testframework.realm.ManagedRealm;
import org.keycloak.testframework.server.KeycloakServerConfig;
import org.keycloak.testframework.server.KeycloakServerConfigBuilder;
import org.keycloak.testframework.server.KeycloakUrls;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@KeycloakIntegrationTest(config = MyProviderTest.MyServerConfig.class)
public class MyProviderTest {

    @InjectRealm
    ManagedRealm myrealm;

    @InjectKeycloakUrls
    KeycloakUrls urls;

    @InjectSimpleHttp
    SimpleHttp simpleHttp;

    @Test
    public void myTest() throws IOException {
        URL url = KeycloakUriBuilder.fromUri(myrealm.getBaseUrl()).path("myresource").build().toURL();

        String response = simpleHttp.doGet(url.toString()).header("Accept", "text/plain").asString();

        Assertions.assertEquals("hello", response);
    }

    public static class MyServerConfig implements KeycloakServerConfig {

        @Override
        public KeycloakServerConfigBuilder configure(KeycloakServerConfigBuilder config) {
            return config.dependencyCurrentProject();
        }
    }

}
