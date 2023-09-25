package org.keycloak.cli.oidc.commands.config;

import org.keycloak.cli.oidc.commands.Error;
import org.keycloak.cli.oidc.config.ConfigException;
import org.keycloak.cli.oidc.config.ConfigHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "current")
public class ConfigCurrentCommand implements Runnable {

    @Override
    public void run() {
        try {
            ConfigHandler.get().printCurrentContext();
        } catch (ConfigException e) {
            Error.onError(e);
        }
    }

}
