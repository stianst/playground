package io.github.stianst.gh.utils;

import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.vertx.dynamic.VertxDynamicGraphQLClientBuilder;
import io.vertx.core.Vertx;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class Client implements AutoCloseable {

    private static final Client instance = new Client();

    private GitHub gitHub;

    private Vertx vertx;

    private DynamicGraphQLClient graphQL;

    public static Client getInstance() {
        return instance;
    }

    public GitHub gitHub() throws IOException {
        if (gitHub == null) {
            gitHub = GitHubBuilder.fromEnvironment().withJwtToken(TokenUtil.token()).build();
        }
        return gitHub;
    }

    public DynamicGraphQLClient graphQL() throws IOException {
        if (vertx == null) {
            vertx = Vertx.vertx();
        }
        if (graphQL == null) {
            graphQL = new VertxDynamicGraphQLClientBuilder()
                    .vertx(vertx)
                    .url("https://api.github.com/graphql")
                    .header("Authorization", "bearer " + TokenUtil.token()).build();
        }
        return graphQL;
    }

    @Override
    public void close() throws Exception {
        if (graphQL != null) {
            graphQL.close();
        }
        if (vertx != null) {
            vertx.close();
        }
    }
}
