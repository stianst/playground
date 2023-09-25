package org.keycloak.cli.oidc.commands;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import org.keycloak.cli.oidc.commands.config.ConfigCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {
        TokenCommand.class,
        ConfigCommand.class
})
public class EntryCommand {
}
