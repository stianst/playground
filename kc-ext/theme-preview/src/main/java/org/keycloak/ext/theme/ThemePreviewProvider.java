package org.keycloak.ext.theme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ThemePreviewProvider implements RealmResourceProvider {

    private List<Page> pages = List.of(
            new Page("login", "Login - username and password", LoginFormsProvider::createLoginUsernamePassword),
            new Page("login-username", "Login - username", LoginFormsProvider::createLoginUsername),
            new Page("login-otp", "Login - OTP", LoginFormsProvider::createLoginTotp),
            new Page("login-config-totp", "Action - OTP", l -> l.createResponse(UserModel.RequiredAction.CONFIGURE_TOTP)),
            new Page("login-update-password", "Action - Update password", l -> l.createResponse(UserModel.RequiredAction.UPDATE_PASSWORD))
    );

    private KeycloakSession session;

    public ThemePreviewProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response list() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><body>\n");
        sb.append("<ul>\n");

        for (Page p : pages) {
            sb.append("<li><a href=\"" + p.template + "\">" + p.description + "</a></li>\n");
        }

        sb.append("</ul>\n");
        sb.append("\n</body></html>");

        return Response.ok(sb.toString()).build();
    }

    @GET
    @Path("/{template}")
    @Produces(MediaType.TEXT_HTML)
    public Response view(@PathParam("template") String page) {
        RealmProxyInvocationHandler proxyHandler = new RealmProxyInvocationHandler(session);

        RealmModel proxy = (RealmModel) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{RealmModel.class}, proxyHandler);
        session.getContext().setRealm(proxy);

        LoginFormsProvider loginForms = session.getProvider(LoginFormsProvider.class);

        loginForms.setActionUri(session.getContext().getUri().getRequestUri());
        loginForms.setAuthenticationSession(new DummyAuthenticationSessionModel());
        loginForms.setUser(new DummyUserModel());

        Optional<Page> pageHandler = pages.stream().filter(p -> p.template.equals(page)).findFirst();

        if (pageHandler.isPresent()) {
            return pageHandler.get().function.apply(loginForms);
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{template}")
    @Produces(MediaType.TEXT_HTML)
    public Response post(@PathParam("template") String page) {
        return view(page);
    }

    @Override
    public void close() {

    }

    public class Page {

        String template;
        String description;
        Function<LoginFormsProvider, Response> function;

        public Page(String template, String description, Function<LoginFormsProvider, Response> function) {
            this.template = template;
            this.description = description;
            this.function = function;
        }
    }

}
