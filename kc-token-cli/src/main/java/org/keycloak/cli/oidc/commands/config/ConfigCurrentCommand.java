package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.commands.Error;
import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "current")
public class ConfigCurrentCommand implements Runnable {

    @CommandLine.Option(names = {"--brief"}, description = "Show brief output")
    boolean brief;

    @Override
    public void run() {
        try {
            ConfigHandler.get().printCurrentContext(brief);
        } catch (ConfigException e) {
            Error.onError(e);
        }
    }

}
