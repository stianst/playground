package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "update")
public class ConfigUpdateCommandAbstract extends AbstractConfigUpdaterCommandAbstract implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context name", required = true)
    String context;

    @Override
    public void run() {
        ConfigHandler configHandler = ConfigHandler.get();

        Context context = ConfigHandler.get().getContext(this.context);
        update(context);

        configHandler.set(context).save();
    }

}
