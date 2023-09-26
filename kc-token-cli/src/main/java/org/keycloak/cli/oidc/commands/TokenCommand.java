package org.keycloak.cli.oidc.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.cli.oidc.User;
import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.kubectl.ExecCredentialRepresentation;
import org.keycloak.cli.oidc.oidc.TokenManager;
import org.keycloak.cli.oidc.oidc.TokenParser;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import picocli.CommandLine;

@CommandLine.Command(name = "token")
public class TokenCommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context to use")
    String contextName;
    @CommandLine.Option(names = {"--type"}, description = "Token type to return")
    String tokenType;
    @CommandLine.Option(names = {"--decode"}, description = "Decode token", defaultValue = "false")
    boolean decode;
    @CommandLine.Option(names = {"--offline"}, description = "Offline mode", defaultValue = "false")
    boolean offline;
    @CommandLine.Option(names = {"--kubectl"}, description = "Kubectl mode", defaultValue = "false")
    boolean kubectl;

    @Override
    public void run() {
        if (!kubectl && System.getenv().containsKey("KUBERNETES_EXEC_INFO")) {
            kubectl = true;
        }

        if (tokenType == null) {
            tokenType = kubectl ? "id" : "access";
        }

        try {
            String token = getToken(tokenType);

            if (kubectl) {
                ExecCredentialRepresentation execCredential = new ExecCredentialRepresentation();
                execCredential.getStatus().setToken(token);

                // TODO Maybe set expiration time, not sure, as we're caching tokens anyways
                // TODO JWT jwt = TokenParser.parse(token).getJWT();
                // TODO execCredential.getStatus().setExpirationTimestamp();

                ObjectMapper objectMapper = new ObjectMapper();
                User.cli().print(objectMapper.writeValueAsString(execCredential));
            } else if (decode) {
                String decoded = TokenParser.parse(token).decoded();
                User.cli().print(decoded);
            } else {
                User.cli().print(token);
            }
        } catch (Exception e) {
            Error.onError(e);
        }
    }

    private String getToken(String tokenType) throws OpenIDException, ConfigException {
        ConfigHandler configHandler = ConfigHandler.get();
        Context context = contextName != null ? configHandler.getContext(contextName) : configHandler.getCurrentContext();
        TokenManager tokenManager = new TokenManager(context, configHandler);
        return tokenManager.getToken(tokenType, offline);
    }

}
