package io.github.stianst.gh.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TokenUtil {

    private static final String[] AUTH_ENV_KEYS = { "GITHUB_TOKEN", "GH_TOKEN", "GITHUB_OAUTH" };

    public static String token() throws IOException {
        String token = tokenFromEnv();
        if (token != null) {
            return token;
        }
        token = tokenFromGhCli();
        if (token != null) {
            return token;
        }

        throw new RuntimeException("Failed to get token for GitHub APIs");
    }

    private static String tokenFromEnv() {
        for (String k : AUTH_ENV_KEYS) {
            String v = System.getenv(k);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private static String tokenFromGhCli() throws IOException {
        return new String(Runtime.getRuntime().exec("gh auth token").getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
    }

}
