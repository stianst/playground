package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import org.keycloak.ext.cli.oidc.config.Context;
import org.keycloak.ext.cli.oidc.oidc.OpenIDFlow;
import picocli.CommandLine;

@CommandLine.Command(name = "context-set")
public class ContextSetCommand extends CommonOptions implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name", required = true)
    String name;

    @Override
    public void run() {
        Context context = new Context();
        context.setName(name);
        context.setIssuer(iss);
        context.setFlow(OpenIDFlow.valueOf(flow.replace('-', '_').toUpperCase()));
        context.setClientId(clientId);
        context.setClientSecret(clientSecret);
        context.setUsername(user);
        context.setUserPassword(password);
        context.setStoreTokens(storeTokens);

        ConfigHandler.get().set(context).save();
    }

}
