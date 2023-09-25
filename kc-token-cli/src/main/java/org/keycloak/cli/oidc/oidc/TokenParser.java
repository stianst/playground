package org.keycloak.cli.oidc.oidc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.cli.oidc.oidc.representations.JWT;

import java.io.IOException;
import java.util.Base64;

public class TokenParser {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JWT jwt;

    private TokenParser(String token) {
        String[] split = token.split("\\.");
        try {
            jwt = new ObjectMapper().readValue(Base64.getDecoder().decode(split[1]), JWT.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TokenParser parse(String token) {
        return new TokenParser(token);
    }

    public JWT getJWT() {
        return jwt;
    }

    public String decoded() {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jwt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
