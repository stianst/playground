package org.keycloak.cli.oidc.kubectl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.cli.oidc.Output;
import org.keycloak.cli.oidc.commands.Error;
import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.kubectl.ExecCredentialRepresentation;
import org.keycloak.cli.oidc.oidc.OpenIDClient;
import org.keycloak.cli.oidc.oidc.TokenParser;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.representations.JWT;
import picocli.CommandLine;

@CommandLine.Command(name = "kubectl")
public class KubectlCommand implements Runnable {

    private String KUBERNETES_EXEC_INFO = "KUBERNETES_EXEC_INFO";

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context to use")
    String contextName;

    @Override
    public void run() {
        try {
            String token = getToken();
            JWT jwt = TokenParser.parse(token).getJWT();

            ExecCredentialRepresentation execCredential = new ExecCredentialRepresentation();
            execCredential.getStatus().setToken(token);

            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(execCredential));
        } catch (Exception e) {
            Error.onError(e);
        }
    }

    public String getToken() throws OpenIDException, ConfigException {
        ConfigHandler configHandler = ConfigHandler.get();
        Context context = contextName != null ? configHandler.getContext(contextName) : configHandler.getCurrentContext();
        OpenIDClient openIDClient = OpenIDClient.create(configHandler, context);
        return openIDClient.getToken("id", false);
    }

}
