package org.keycloak.cli.oidc;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class User {

    private static CLI cli;

    private static Web web;

    public static CLI cli() {
        if (cli == null) {
            cli = new CLI();
        }
        return cli;
    }

    public static Web web() {
        if (web == null) {
            web = new Web();
        }
        return web;
    }

    public static class CLI {

        public void print(String... lines) {
            for (String line : lines) {
                System.out.println(line);
            }
        }

    }

    public static class Web {

        public boolean isDesktopSupported() {
            // return Desktop.isDesktopSupported();
            return true;
        }

        // TODO Only supports Linux at the moment, can't use Desktop.getDesktop().browse(uri) as it results in output, and
        // doesn't work with native
        public void browse(URI uri) throws IOException {
            String[] command = new String[] {
              "xdg-open",
              uri.toString()
            };
            Process exec = Runtime.getRuntime().exec(command);
            try {
                exec.waitFor(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (exec.exitValue() != 0) {
                throw new IOException("Failed to launch browser");
            }
        }
    }

}
