package org.keycloak.ext.theme;

import org.keycloak.models.ClientModel;
import org.keycloak.models.RealmModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class DummyClientModel {

    public static ClientModel create(RealmModel realm) {
        return (ClientModel) Proxy.newProxyInstance(
                DummyClientModel.class.getClassLoader(),
                new Class[]{ClientModel.class},
                new ClientInvocationHandler(realm)
        );
    }

    private static class ClientInvocationHandler implements InvocationHandler {

        private final RealmModel realm;

        public ClientInvocationHandler(RealmModel realm) {
            this.realm = realm;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();

            // Handle specific methods
            switch (methodName) {
                case "getId":
                case "getClientId":
                    return "dummy-client";
                case "getName":
                    return "Dummy Client";
                case "getDescription":
                    return "Dummy client for theme preview";
                case "getRealm":
                    return realm;
                case "getProtocol":
                    return "openid-connect";
                case "isEnabled":
                case "isPublicClient":
                case "isStandardFlowEnabled":
                    return true;
            }

            // Handle by return type
            if (returnType == boolean.class || returnType == Boolean.class) {
                return false;
            } else if (returnType == int.class || returnType == Integer.class) {
                return 0;
            } else if (returnType == String.class) {
                return null;
            } else if (returnType == Set.class) {
                return Collections.emptySet();
            } else if (returnType == Map.class) {
                return Collections.emptyMap();
            } else if (returnType == Stream.class) {
                return Stream.empty();
            } else if (returnType == void.class) {
                return null;
            }

            return null;
        }
    }
}
