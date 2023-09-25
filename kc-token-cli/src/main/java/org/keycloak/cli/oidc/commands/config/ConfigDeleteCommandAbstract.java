package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "delete")
public class ConfigDeleteCommandAbstract extends AbstractCommonOptionsCommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context name", required = true)
    String context;

    @Override
    public void run() {
        ConfigHandler.get().delete(context).save();
    }

}
