package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import org.keycloak.ext.cli.oidc.config.Context;
import picocli.CommandLine;

@CommandLine.Command(name = "context-set")
public class ContextSetCommand extends AbstractContextUpdater implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name", required = true)
    String name;

    @Override
    public void run() {
        Context context = new Context();
        context.setName(name);
        update(context);

        ConfigHandler.get().set(context).save();
    }

}