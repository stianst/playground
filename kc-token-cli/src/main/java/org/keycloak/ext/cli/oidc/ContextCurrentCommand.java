package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import org.keycloak.ext.cli.oidc.config.Context;
import picocli.CommandLine;

@CommandLine.Command(name = "context-current")
public class ContextCurrentCommand implements Runnable {

    @Override
    public void run() {
        ConfigHandler.get().printCurrentContext();
    }

}
