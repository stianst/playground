package com.example.keycloak.clientpolicy;

import com.cedarpolicy.BasicAuthorizationEngine;
import com.example.keycloak.cedar.CedarProvider;
import com.example.keycloak.cedar.DefaultCedarProvider;
import com.example.keycloak.cedar.FilePolicySet;
import com.example.keycloak.clientpolicy.executor.CedarTokenExchangeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class CedarTokenExchangeRequestTest {

    private static CedarProvider CEDAR;

    @BeforeAll
    public static void beforeAll() {
        FilePolicySet filePolicySet = new FilePolicySet(new File("/home/st/dev/keycloak-cedar-poc/samples/token-exchange.cedar"));
        CEDAR = new DefaultCedarProvider(new BasicAuthorizationEngine(), filePolicySet.getPolicySet());
    }

    @Test
    void testPermitted() {
        boolean permitted = CEDAR.request(CedarTokenExchangeRequest.class).issuer("https://myissuer").client("myclient").audience(List.of("aud1")).evalute();
        Assertions.assertTrue(permitted);
    }

    @Test
    void testForbiddenClient() {
        boolean permitted = CEDAR.request(CedarTokenExchangeRequest.class).issuer("https://myissuer").client("myclient2").evalute();
        Assertions.assertFalse(permitted);
    }

    @Test
    void testForbiddenAud() {
        boolean permitted = CEDAR.request(CedarTokenExchangeRequest.class).issuer("https://myissuer").client("myclient").audience(List.of("aud2")).evalute();
        Assertions.assertFalse(permitted);
    }

}
