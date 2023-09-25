package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "view")
public class ConfigViewCommand implements Runnable {

    @CommandLine.Option(names = {"-c", "--context"}, description = "Context name")
    String context;
    @CommandLine.Option(names = {"--brief"}, description = "Show brief output")
    boolean brief;


    @Override
    public void run() {
        if (context != null) {
            ConfigHandler.get().printContext(context, brief);
        } else {
            ConfigHandler.get().printContexts(brief);
        }
    }

}
