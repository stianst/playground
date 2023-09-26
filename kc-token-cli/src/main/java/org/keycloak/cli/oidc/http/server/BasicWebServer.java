package org.keycloak.cli.oidc.http.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicWebServer {

    private ServerSocket serverSocket;

    public static BasicWebServer start() throws IOException {
        BasicWebServer webServer = new BasicWebServer();
        webServer.serverSocket = new ServerSocket(0, 10, InetAddress.getByName("127.0.0.1"));
        return webServer;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public HttpRequest accept() throws IOException {
        Socket socket = serverSocket.accept();
        return new HttpRequest(socket);
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

}
