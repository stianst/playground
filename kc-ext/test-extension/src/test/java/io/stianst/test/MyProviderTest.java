package io.stianst.test;

import org.junit.jupiter.api.Test;
import org.keycloak.testframework.annotations.KeycloakIntegrationTest;
import org.keycloak.testframework.server.KeycloakServerConfig;
import org.keycloak.testframework.server.KeycloakServerConfigBuilder;

@KeycloakIntegrationTest(config = MyProviderTest.MyServerConfig.class)
public class MyProviderTest {

    @Test
    public void myTest() {

    }

    public static class MyServerConfig implements KeycloakServerConfig {

        @Override
        public KeycloakServerConfigBuilder configure(KeycloakServerConfigBuilder config) {
            return config.dependency("playground.stianst.github.io", "myextension", true);
        }
    }

}
