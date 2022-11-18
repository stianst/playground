package org.keycloak.examples;

import org.infinispan.Cache;
import org.keycloak.connections.infinispan.InfinispanConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class CacheStatisticsRealmResourceProvider implements RealmResourceProvider {

    final String[] caches = new String[] {
            InfinispanConnectionProvider.REALM_CACHE_NAME,
            InfinispanConnectionProvider.REALM_REVISIONS_CACHE_NAME,
            InfinispanConnectionProvider.USER_CACHE_NAME,
            InfinispanConnectionProvider.USER_REVISIONS_CACHE_NAME,
            InfinispanConnectionProvider.USER_SESSION_CACHE_NAME,
            InfinispanConnectionProvider.CLIENT_SESSION_CACHE_NAME,
            InfinispanConnectionProvider.OFFLINE_USER_SESSION_CACHE_NAME,
            InfinispanConnectionProvider.OFFLINE_CLIENT_SESSION_CACHE_NAME,
            InfinispanConnectionProvider.LOGIN_FAILURE_CACHE_NAME,
            InfinispanConnectionProvider.AUTHENTICATION_SESSIONS_CACHE_NAME,
            InfinispanConnectionProvider.AUTHORIZATION_CACHE_NAME,
            InfinispanConnectionProvider.AUTHORIZATION_REVISIONS_CACHE_NAME,
            InfinispanConnectionProvider.ACTION_TOKEN_CACHE,
            InfinispanConnectionProvider.KEYS_CACHE_NAME,
            InfinispanConnectionProvider.WORK_CACHE_NAME
    };

    private KeycloakSession session;

    public CacheStatisticsRealmResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/stats")
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        InfinispanConnectionProvider inf = session.getProvider(InfinispanConnectionProvider.class);
        for (String c : caches) {
            Cache<Object, Object> cache = inf.getCache(c);
            fixedWidth(c, 25, sb);
            sb.append(cache.size());
            sb.append("\n");
        }
        return sb.toString();
    }

    @GET
    @Path("/clear")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearCache() {
        InfinispanConnectionProvider inf = session.getProvider(InfinispanConnectionProvider.class);
        for (String c : caches) {
            System.out.println("Clearing " + c);
            Cache<Object, Object> cache = inf.getCache(c);
            cache.clear();
        }
        return "okay";
    }

    @GET
    @Path("/clear/{cache}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearCache(@PathParam("cache") String cache) {
        System.out.println("Clearing " + cache);
        InfinispanConnectionProvider inf = session.getProvider(InfinispanConnectionProvider.class);
        Cache<Object, Object> c = inf.getCache(cache);
        c.clear();
        return "okay";
    }

    @Override
    public void close() {
    }

    private void fixedWidth(String s, int l, StringBuilder sb) {
        sb.append(s);
        for (int k = s.length(); k < l; k++) {
            sb.append(" ");
        }
    }

}
