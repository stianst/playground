package org.keycloak.cli.oidc.http.server;

import org.keycloak.cli.oidc.http.MimeType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Socket socket;
    private String method;
    private String path;
    private String protocol;
    private Map<String, String> queryParams;
    private Map<String, String> headerParams;

    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String firstLine = br.readLine();
        if (firstLine != null) {
            String[] request = firstLine.split(" ");

            method = request[0];
            path = request[1];
            protocol = request[2];

            queryParams = new HashMap<>();
            headerParams = new HashMap<>();

            int queryIndex = path.indexOf('?');
            if (queryIndex > 0) {
                String[] rawQuery = path.substring(queryIndex + 1).split("&");
                path = path.substring(0, queryIndex);

                for (String query : rawQuery) {
                    String[] split = query.split("=");
                    queryParams.put(URLDecoder.decode(split[0], StandardCharsets.UTF_8), URLDecoder.decode(split[1], StandardCharsets.UTF_8));
                }
            }

            for (String header = br.readLine(); header != null && !header.equals(""); header = br.readLine()) {
                String[] split = header.split(": ");
                if (split.length == 2) {
                    headerParams.put(split[0], split[1]);
                }
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public void ok(byte[] body, MimeType contentType) throws IOException {
        HttpResponse.ok(body, contentType).send(socket);
    }

    public void badRequest() throws IOException {
        HttpResponse.badRequest().send(socket);
    }

}
