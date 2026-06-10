package io.github.stianst.gh;

import io.github.stianst.gh.utils.Client;
import org.jboss.logging.Logger;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GhTests {

    private static final Logger LOG = Logger.getLogger(GhTests.class);

    public static void main(String[] args) throws IOException {
        GitHub gitHub = Client.getInstance().gitHub();
        GHRepository repository = gitHub.getRepository("keycloak/keycloak");

        GHIssue issue = repository.getIssue(49442);

        gitHub.


    }

}
