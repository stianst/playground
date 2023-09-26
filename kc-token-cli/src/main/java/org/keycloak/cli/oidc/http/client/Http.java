package org.keycloak.cli.oidc.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.cli.oidc.http.MimeType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Http implements AutoCloseable {

    private String endpoint;
    private HttpURLConnection connection;
    private String userAgent = "kc-http/1.0";
    private String authorization;
    private MimeType accept;
    private MimeType contentType;
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> bodyParams = new HashMap<>();

    private Http(String endpoint) {
        this.endpoint = endpoint;
    }

    public static Http create(String endpoint) {
        return new Http(endpoint);
    }

    public Http accept(MimeType type) {
        this.accept = type;
        return this;
    }

    public Http contentType(MimeType type) {
        this.contentType = type;
        return this;
    }

    public Http query(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public Http body(String key, String value) {
        bodyParams.put(key, value);
        return this;
    }

    public Http userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Http authorization(String username, String password) {
        authorization = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return this;
    }

    public String asString() throws IOException {
        return new String(connect().readAllBytes(), StandardCharsets.UTF_8);
    }

    public <T> T asObject(Class<T> clazz) throws IOException {
        return new ObjectMapper().readValue(connect(), clazz);
    }

    private InputStream connect() throws IOException {
        createConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        if (authorization != null) {
            connection.setRequestProperty("Authorization", authorization);
        }
        sendBodyIfAvailable();
        return connection.getInputStream();
    }

    private void createConnection() throws IOException {
        String url;
        if (queryParams.isEmpty()) {
            url = endpoint;
        } else {
            StringBuilder sb = new StringBuilder(endpoint);
            sb.append("?");
            sb.append(encodeParams(queryParams));
            url = sb.toString();
        }

        connection = (HttpURLConnection) new URL(url).openConnection();
        if (accept != null) {
            connection.setRequestProperty("Accept", accept.toString());
        }
    }

    private void sendBodyIfAvailable() throws IOException {
        if (contentType == null) {
            return;
        }

        if (contentType.equals(MimeType.FORM)) {
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType.toString());

            byte[] body = encodeParams(bodyParams).getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", Integer.toString(body.length));
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(body);
            }
        }
    }

    private String encodeParams(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (!map.isEmpty()) {
            Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> q = itr.next();
                sb.append(URLEncoder.encode(q.getKey(), StandardCharsets.UTF_8));
                sb.append("=");
                sb.append(URLEncoder.encode(q.getValue(), StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
