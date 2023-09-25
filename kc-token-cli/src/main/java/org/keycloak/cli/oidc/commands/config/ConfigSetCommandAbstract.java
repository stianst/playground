package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.commands.Error;
import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import org.keycloak.cli.oidc.config.Context;
import picocli.CommandLine;

@CommandLine.Command(name = "set")
public class ConfigSetCommandAbstract extends AbstractConfigUpdaterCommandAbstract implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context name", required = true)
    String context;

    @Override
    public void run() {
        Context context = new Context();
        context.setName(this.context);
        update(context);

        try {
            ConfigHandler.get().set(context).save();
        } catch (ConfigException e) {
            Error.onError(e);
        }
    }

}
