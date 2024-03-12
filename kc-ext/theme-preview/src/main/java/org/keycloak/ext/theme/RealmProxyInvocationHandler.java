package org.keycloak.ext.theme;

import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class RealmProxyInvocationHandler implements InvocationHandler {

    private final RealmModel parent;

    private List<MethodHandler> methodHandlers = List.of(
            new BooleanMethodHandler(),
            new StringMethodHandler()
    );

    private final MultivaluedMap<String, String> queryParameters;

    public RealmProxyInvocationHandler(KeycloakSession session) {
        this.parent = session.getContext().getRealm();
        this.queryParameters = session.getContext().getHttpRequest().getUri().getQueryParameters();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args == null) {
            for (MethodHandler h : methodHandlers) {
                if (h.supported(method)) {
                    Object value = h.value(method);
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        return method.invoke(parent, args);
    }

    interface MethodHandler<T> {

        boolean supported(Method method);

        T value(Method method);

    }

    class BooleanMethodHandler implements MethodHandler<Boolean> {

        @Override
        public boolean supported(Method method) {
            Class<?> returnType = method.getReturnType();
            return boolean.class.equals(returnType) || Boolean.class.equals(returnType);
        }

        @Override
        public Boolean value(Method method) {
            String methodName = method.getName();
            String key = methodName.substring(2, 3).toLowerCase(Locale.ENGLISH) + methodName.substring(3);
            String value = queryParameters.getFirst("realm." + key);
            return value != null ? Boolean.valueOf(value) : null;
        }
    }

    class StringMethodHandler implements MethodHandler<String> {

        @Override
        public boolean supported(Method method) {
            Class<?> returnType = method.getReturnType();
            return String.class.equals(returnType);
        }

        @Override
        public String value(Method method) {
            String methodName = method.getName();
            String key = methodName.substring(3, 4).toLowerCase(Locale.ENGLISH) + methodName.substring(4);
            return queryParameters.getFirst("realm." + key);
        }
    }

}
