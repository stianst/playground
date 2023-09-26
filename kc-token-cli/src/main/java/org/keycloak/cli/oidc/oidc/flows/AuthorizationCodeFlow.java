package org.keycloak.cli.oidc.oidc.flows;

import org.keycloak.cli.oidc.User;
import org.keycloak.cli.oidc.config.Context;
import org.keycloak.cli.oidc.http.MimeType;
import org.keycloak.cli.oidc.http.UriBuilder;
import org.keycloak.cli.oidc.http.server.BasicWebServer;
import org.keycloak.cli.oidc.http.server.HttpRequest;
import org.keycloak.cli.oidc.http.server.HttpResponse;
import org.keycloak.cli.oidc.oidc.PKCE;
import org.keycloak.cli.oidc.oidc.TokenParser;
import org.keycloak.cli.oidc.oidc.exceptions.OpenIDException;
import org.keycloak.cli.oidc.oidc.representations.JWT;
import org.keycloak.cli.oidc.oidc.representations.TokenResponse;
import org.keycloak.cli.oidc.oidc.representations.WellKnown;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.UUID;

public class AuthorizationCodeFlow extends AbstractFlow {

    public AuthorizationCodeFlow(Context context, WellKnown wellKnown) {
        super(context, wellKnown);
    }

    public TokenResponse execute() throws OpenIDException {
        if (!User.web().isDesktopSupported()) {
            throw new OpenIDException("Web browser not available");
        }

        BasicWebServer webServer;
        try {
            webServer = BasicWebServer.start();
        } catch (IOException e) {
            throw new OpenIDException("Failed to start callback server", e);
        }

        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();
        String redirectUri = "http://127.0.0.1:" + webServer.getPort() + "/callback";
        PKCE pkce = PKCE.create();

        URI uri = UriBuilder.create(wellKnown.getAuthorizationEndpoint())
                .query("scope", "openid")
                .query("response_type", "code")
                .query("client_id", context.getClientId())
                .query("redirect_uri", redirectUri)
                .query("state", state)
                .query("nonce", nonce)
                .query("code_challenge", pkce.getCodeChallenge())
                .query("code_challenge_method", "S256")
                .toURI();

        try {
            User.web().browse(uri);
        } catch (IOException e) {
            throw new OpenIDException("Failed to open web browser", e);
        }

        HttpRequest callback;
        try {
            callback = waitForCallback(webServer);
            webServer.stop();
        } catch (IOException e) {
            throw new OpenIDException("Failed to process callback", e);
        }

        try {
            if (callback.getQueryParams().containsKey("error")) {
                throw new OpenIDException("Authentication request failed: " + callback.getQueryParams().get("error"));
            }

            String code = callback.getQueryParams().get("code");
            String returnedState = callback.getQueryParams().get("state");

            if (!state.equals(returnedState)) {
                throw new OpenIDException("Invalid state parameter returned");
            }

            TokenResponse tokenResponse = clientRequest(wellKnown.getTokenEndpoint())
                    .contentType(MimeType.FORM)
                    .body("grant_type", "authorization_code")
                    .body("code", code)
                    .body("scope", "openid")
                    .body("redirect_uri", redirectUri)
                    .body("code_verifier", pkce.getCodeVerifier())
                    .asObject(TokenResponse.class);

            JWT idToken = TokenParser.parse(tokenResponse.getIdToken()).getJWT();
            if (!nonce.equals(idToken.getClaims().get("nonce"))) {
                throw new OpenIDException("Invalid nonce parameter returned");
            }

            return tokenResponse;
        } catch (IOException e) {
            throw new OpenIDException("Failed to send authentication request");
        }
    }

    private HttpRequest waitForCallback(BasicWebServer webServer) throws IOException {
        while (true) {
            HttpRequest httpRequest = webServer.accept();

            if (httpRequest.getPath().equals("/favicon.ico")) {
                byte[] body = BasicWebServer.class.getResource("favicon.ico").openStream().readAllBytes();
                httpRequest.ok(body, MimeType.X_ICON);
            } else if (httpRequest.getPath().equals("/callback")) {
                byte[] body = BasicWebServer.class.getResource("callback.html").openStream().readAllBytes();
                httpRequest.ok(body, MimeType.HTML);
                return httpRequest;
            } else {
                httpRequest.badRequest();
            }
        }
    }

}
