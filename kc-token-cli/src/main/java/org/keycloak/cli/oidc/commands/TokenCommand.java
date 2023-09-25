package org.keycloak.cli.oidc.commands;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.oidc.OpenIDClient;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.Output;
import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.oidc.TokenParser;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "token")
public class TokenCommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context to use")
    String contextName;
    @CommandLine.Option(names = {"--type"}, description = "Token type to return", defaultValue = "access")
    String tokenType;
    @CommandLine.Option(names = {"--decode"}, description = "Decode token", defaultValue = "false")
    boolean decode;
    @CommandLine.Option(names = {"--offline"}, description = "Offline mode", defaultValue = "false")
    boolean offline;

    @Override
    public void run() {
        String token = getToken();
        if (decode) {
            String decoded = TokenParser.parse(token).decoded();
            Output.println(decoded);
        } else {
            Output.println(token);
        }
    }

    public String getToken() {
        ConfigHandler configHandler = ConfigHandler.get();
        Context context = contextName != null ? configHandler.getContext(contextName) : configHandler.getCurrentContext();
        OpenIDClient openIDClient = OpenIDClient.create(context);

        boolean refresh = tokenType.equals("refresh");
        String savedToken = getSaved(context, tokenType);

        if (isValid(savedToken)) {
            return savedToken;
        } else if (offline) {
            throw new RuntimeException("Token expired");
        }

        TokenResponse tokenResponse = null;
        if (!refresh) {
            String refreshToken = context.getRefreshToken();
            if (isValid(refreshToken)) {
                tokenResponse = openIDClient.refresh(refreshToken);
            }
        }

        if (tokenResponse == null) {
            tokenResponse = openIDClient.token();
        }

        if (context.isStoreTokens() == null || context.isStoreTokens()) {
            context.setRefreshToken(tokenResponse.getRefreshToken());
            context.setIdToken(tokenResponse.getIdToken());
            context.setAccessToken(tokenResponse.getAccessToken());
            configHandler.save();
        }

        return getToken(tokenResponse, tokenType);
    }

    private boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        HashMap<String, String> claims = TokenParser.parse(token).getClaims();
        long exp = TimeUnit.SECONDS.toMillis(Long.valueOf(claims.get("exp")));
        return exp > System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
    }

    private String getSaved(Context context, String tokenType) {
        switch (tokenType) {
            case "id":
                return context.getIdToken();
            case "access":
                return context.getAccessToken();
            case "refresh":
                return context.getRefreshToken();
        }
        throw new RuntimeException("Unknown token type");
    }

    private String getToken(TokenResponse tokenResponse, String tokenType) {
        switch (tokenType) {
            case "id":
                return tokenResponse.getIdToken();
            case "access":
                return tokenResponse.getAccessToken();
            case "refresh":
                return tokenResponse.getRefreshToken();
        }
        throw new RuntimeException("Unknown token type");
    }

}
