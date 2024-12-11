package org.keycloak.examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpiffeAuthenticatorTest {

    @Test
    public void test() throws Exception {
        String assertion = "eyJhbGciOiJFUzI1NiIsImtpZCI6IkIzOXpDMjExMkMyNEgzeFlnVmVZSmpZSVJZN2xYNWdtIiwidHlwIjoiSldUIn0.eyJhdWQiOlsic3BpZmZlOi8vZXhhbXBsZS5vcmcvbXljbGllbnQiXSwiZXhwIjoxNzI4NDYxNzk1LCJpYXQiOjE3Mjg0NjE0OTUsInN1YiI6InNwaWZmZTovL2V4YW1wbGUub3JnL215Y2xpZW50In0.dv8M8i5Ac2QviaFmhI92v6BWl6koHNY48WLgVXwi-ByPwg1Wxy18Ky3IO8b49qsddpwnGlh6lA3m_sluDRnKGg";
        SpiffeConfig spiffeConfig = new SpiffeConfig("example.org", "file:///home/st/dev/playground/kc-ext/spiffe-authenticator/target/tmp/spire/keys.json", "myclient");

        SpiffeAuthenticator spiffeAuthenticator = new SpiffeAuthenticator(assertion, spiffeConfig);
        Assertions.assertTrue(spiffeAuthenticator.verify());
    }

}
