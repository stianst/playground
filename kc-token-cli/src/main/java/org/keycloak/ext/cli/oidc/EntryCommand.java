package org.keycloak.ext.cli.oidc;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {
        TokenCommand.class,
        ContextSetCommand.class,
        ContextUpdateCommand.class,
        ContextUseCommand.class,
        ContextCurrentCommand.class,
        ContextViewCommand.class,
        ContextDeleteCommand.class
})
public class EntryCommand {
}
