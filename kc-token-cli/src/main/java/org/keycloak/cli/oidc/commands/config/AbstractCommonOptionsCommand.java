package org.keycloak.cli.oidc.commands.config;

import picocli.CommandLine;

public abstract class AbstractCommonOptionsCommand {

    @CommandLine.Option(names = {"--issuer"}, description = "Issuer URL")
    String iss;

    @CommandLine.Option(names = {"--flow"}, description = "Flow")
    String flow;

    @CommandLine.Option(names = {"--client-id"}, description = "Client ID")
    String clientId;

    @CommandLine.Option(names = {"--client-secret"}, description = "Client secret")
    String clientSecret;

    @CommandLine.Option(names = {"--user"}, description = "User name for resource-owner flow")
    String user;

    @CommandLine.Option(names = {"--user-password"}, description = "User password for resource-owner flow")
    String password;

    @CommandLine.Option(names = {"--store-tokens"}, description = "Store tokens")
    String storeTokens;

}
