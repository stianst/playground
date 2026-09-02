package com.example.keycloak.clientpolicy;

import com.cedarpolicy.model.exception.InternalException;
import com.example.keycloak.clientpolicy.executor.Cedar;
import com.example.keycloak.clientpolicy.executor.CedarTokenExchangeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CedarTokenExchangeRequestTest {

    private static Cedar CEDAR;

    @BeforeAll
    public static void beforeAll() throws IOException, InternalException {
        CEDAR = new Cedar(new File("/home/st/dev/keycloak-cedar-poc/samples/token-exchange.cedar"));
    }

    @Test
    void testPermitted() {
        boolean permitted = CedarTokenExchangeRequest.create(CEDAR, "https://myissuer", "myclient").withAudience(List.of("aud1")).evalute();
        Assertions.assertTrue(permitted);
    }

    @Test
    void testForbiddenClient() {
        boolean permitted = CedarTokenExchangeRequest.create(CEDAR, "https://myissuer", "myclient2").evalute();
        Assertions.assertFalse(permitted);
    }

    @Test
    void testForbiddenAud() {
        boolean permitted = CedarTokenExchangeRequest.create(CEDAR, "https://myissuer", "myclient").withAudience(List.of("aud2")).evalute();
        Assertions.assertFalse(permitted);
    }

}
