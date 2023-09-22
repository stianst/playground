package org.keycloak.ext.cli.oidc.oidc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class TokenParser {

    private ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<String, String> claims;

    private TokenParser(String token) {
        String[] split = token.split("\\.");
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};
        try {
            claims = new ObjectMapper().readValue(Base64.getDecoder().decode(split[1]), typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TokenParser parse(String token) {
        return new TokenParser(token);
    }

    public HashMap<String, String> getClaims() {
        return claims;
    }

    public String decoded() {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
