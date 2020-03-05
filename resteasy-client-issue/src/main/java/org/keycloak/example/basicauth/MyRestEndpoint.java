package org.keycloak.example.basicauth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class MyRestEndpoint {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>Token</h1>");
        sb.append("<pre>" + new RestEasyTester().test() + "</pre>");

        sb.append("</body></html>");

        return sb.toString();
    }

}
