package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import org.keycloak.ext.cli.oidc.config.Context;
import org.keycloak.ext.cli.oidc.oidc.OpenIDFlow;
import picocli.CommandLine;

@CommandLine.Command(name = "context-update")
public class ContextUpdateCommand extends AbstractContextUpdater implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name", required = true)
    String name;

    public static void main(String[] args) {
        ContextUpdateCommand c = new ContextUpdateCommand();
        c.name = "test";
        c.clientId = "null";
        c.storeTokens = "null";
        c.run();
    }

    @Override
    public void run() {
        ConfigHandler configHandler = ConfigHandler.get();

        Context context = ConfigHandler.get().getContext(name);
        update(context);

        configHandler.set(context).save();
    }

}
