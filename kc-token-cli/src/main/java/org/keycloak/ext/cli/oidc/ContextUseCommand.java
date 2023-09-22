package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "context-use")
public class ContextUseCommand implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name", required = true)
    String name;

    @Override
    public void run() {
        ConfigHandler.get().setCurrent(name).save();
    }

}
