package org.keycloak.ext.cli.oidc;

import org.keycloak.ext.cli.oidc.config.ConfigHandler;
import org.keycloak.ext.cli.oidc.config.Context;
import org.keycloak.ext.cli.oidc.oidc.OpenIDFlow;
import picocli.CommandLine;

@CommandLine.Command(name = "context-delete")
public class ContextDeleteCommand extends CommonOptions implements Runnable {

    @CommandLine.Option(names = {"--name"}, description = "Context name", required = true)
    String name;

    @Override
    public void run() {
        ConfigHandler.get().delete(name).save();
    }

}
