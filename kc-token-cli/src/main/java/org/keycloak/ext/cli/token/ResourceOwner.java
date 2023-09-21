package org.keycloak.ext.cli.token;

import picocli.CommandLine;

@CommandLine.Command
public class ResourceOwner implements Runnable {

    @CommandLine.Option(names = {"-i", "--issuer-url"}, description = "Issuer URL")
    String iss;

    @CommandLine.Option(names = {"-c", "--client-id"}, description = "Client ID")
    String clientId;

    @CommandLine.Option(names = {"-s", "--client-secret"}, description = "Client Secret")
    String clientSecret;

    @CommandLine.Option(names = {"-u"}, description = "User name", interactive = true)
    String user;

    @CommandLine.Option(names = {"-p"}, description = "Password", interactive = true)
    String password;

    @Override
    public void run() {
        System.out.println(iss);
        System.out.println(user);
    }
}
