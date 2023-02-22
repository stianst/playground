package org.keycloak.hostname.debug;

import org.keycloak.models.KeycloakSession;
import org.keycloak.quarkus.runtime.Environment;
import org.keycloak.services.Urls;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resources.Cors;
import org.keycloak.theme.FreeMarkerException;
import org.keycloak.theme.Theme;
import org.keycloak.theme.freemarker.FreeMarkerProvider;
import org.keycloak.urls.UrlType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HostnameDebugProvider implements RealmResourceProvider {

    private KeycloakSession session;
    private Map<String, String> config;

    public HostnameDebugProvider(KeycloakSession session, Map<String, String> config) {
        this.session = session;
        this.config = config;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String debug() throws IOException, FreeMarkerException {
        FreeMarkerProvider provider = session.getProvider(FreeMarkerProvider.class);

        URI frontendUri = session.getContext().getUri(UrlType.FRONTEND).getBaseUri();
        URI backendUri = session.getContext().getUri(UrlType.BACKEND).getBaseUri();
        URI adminUri = session.getContext().getUri(UrlType.ADMIN).getBaseUri();

        String frontendTestUrl = getTest(frontendUri);
        String backendTestUrl = getTest(backendUri);
        String adminTestUrl = getTest(adminUri);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("frontendUrl", frontendUri.toString());
        attributes.put("backendUrl", backendUri.toString());
        attributes.put("adminUrl", adminUri.toString());

        attributes.put("realm", session.getContext().getRealm().getName());
        attributes.put("realmUrl", session.getContext().getRealm().getAttribute("frontendUrl"));

        attributes.put("frontendTestUrl", frontendTestUrl);
        attributes.put("backendTestUrl", backendTestUrl);
        attributes.put("adminTestUrl", adminTestUrl);

        attributes.put("serverMode", Environment.isDevMode() ? "dev [start-dev]" : "production [start]");

        attributes.put("config", config);
        attributes.put("headers", getHeaders());

        return provider.processTemplate(attributes, "hostname-debug.ftl", session.theme().getTheme("base", Theme.Type.LOGIN));
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new TreeMap<>();
        HttpHeaders requestHeaders = session.getContext().getRequestHeaders();
        for (String h : Constants.RELEVANT_HEADERS) {
            addProxyHeader(h, headers, requestHeaders);
        }
        return headers;
    }

    private void addProxyHeader(String header, Map<String, String> proxyHeaders, HttpHeaders requestHeaders) {
        String value = requestHeaders.getHeaderString(header);
        if (value != null && !value.isEmpty()) {
            proxyHeaders.put(header, value);
        }
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {
        Response.ResponseBuilder builder = Response.ok("test");
        String origin = session.getContext().getRequestHeaders().getHeaderString(Cors.ORIGIN_HEADER);
        builder.header(Cors.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        builder.header(Cors.ACCESS_CONTROL_ALLOW_METHODS, "GET");
        return builder.build();
    }

    private String getTest(URI baseUri) {
        return Urls.realmBase(baseUri).path("{realm}/{ext}/test").build(session.getContext().getRealm().getName(), HostnameDebugProviderFactory.ID).toString();
    }

    @Override
    public void close() {

    }
}
