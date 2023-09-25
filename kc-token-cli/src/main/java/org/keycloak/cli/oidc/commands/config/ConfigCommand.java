package org.keycloak.cli.oidc.commands.config;

import picocli.CommandLine;

@CommandLine.Command(name = "config", subcommands = {
        ConfigSetCommandAbstract.class,
        ConfigUpdateCommandAbstract.class,
        ConfigUseCommand.class,
        ConfigCurrentCommand.class,
        ConfigViewCommand.class,
        ConfigDeleteCommandAbstract.class,
})
public class ConfigCommand {

}
