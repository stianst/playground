package org.keycloak.cli.oidc.commands;

public class Error {

    public static void onError(Exception e) {
        if (System.getenv().containsKey("DEBUG")) {
            e.printStackTrace();
        } else {
            StringBuilder error = new StringBuilder();
            if (e.getMessage() != null) {
                error.append(e.getMessage());
            } else {
                error.append(e.getClass().getName());
            }
            if (e.getCause() != null) {
                error.append(": ");
                if (e.getCause().getMessage() != null) {
                    error.append(e.getCause().getMessage());
                } else {
                    error.append(e.getCause().getClass().getName());
                }
            }
            System.err.println(error);
        }
        System.exit(1);
    }

}
