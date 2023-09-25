package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "use")
public class ConfigUseCommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context name", required = true)
    String context;

    @Override
    public void run() {
        ConfigHandler.get().setCurrent(context).save();
    }

}
