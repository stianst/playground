package org.keycloak.cli.oidc.commands;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import org.keycloak.cli.oidc.commands.config.ConfigCommand;
import org.keycloak.cli.oidc.kubectl.KubectlCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {
        TokenCommand.class,
        ConfigCommand.class,
        KubectlCommand.class
})
public class EntryCommand {
}
