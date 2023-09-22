package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "context-view")
public class ContextViewCommand implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name")
    String name;

    @Override
    public void run() {
        if (name != null) {
            ConfigHandler.get().printContext(name);
        } else {
            ConfigHandler.get().printContexts();
        }
    }

}
