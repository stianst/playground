package org.keycloak.ext.cli.oidc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.ext.cli.oidc.Output;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

public class ConfigHandler {

    public static void main(String[] args) throws IOException {
        ConfigHandler configHandler = new ConfigHandler();

        configHandler.getContext("resource-owner");

    }

    private static ConfigHandler configHandler;

    private File configFile;
    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    private Config config;

    private ConfigHandler() {
        File userHome = new File(System.getProperty("user.home"));
        File homeDir = new File(userHome,".kc");
        if (!homeDir.isDirectory()) {
            homeDir.mkdir();
        }
        configFile = new File(homeDir, "oidc.yaml");
        load();
    }

    public static ConfigHandler get() {
        if (configHandler == null) {
            configHandler = new ConfigHandler();
        }
        return configHandler;
    }

    private void load() {
        try {
            if (configFile.isFile()) {
                config = objectMapper.readValue(configFile, Config.class);
            }

            if (config == null) {
                config = new Config();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ConfigHandler save() {
        try {
            config.getContexts().sort(Comparator.comparing(Context::getName));

            if (config.getCurrent() == null && config.getContexts().size() == 1) {
                config.setCurrent(config.getContexts().get(0).getName());
            }

            objectMapper.writeValue(configFile, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ConfigHandler setCurrent(String name) {
        if (getContext(name) == null) {
            throw new RuntimeException("Context not found");
        }
        config.setCurrent(name);
        return this;
    }

    public ConfigHandler set(Context context) {
        String current = config.getCurrent();

        delete(context.getName());
        config.getContexts().add(context);

        config.setCurrent(current);
        return this;
    }

    public ConfigHandler delete(String name) {
        Iterator<Context> contextItr = config.getContexts().iterator();
        while (contextItr.hasNext()) {
            if (contextItr.next().getName().equals(name)) {
                contextItr.remove();
            }
        }
        if (config.getCurrent() != null && config.getCurrent().equals(name)) {
            config.setCurrent(null);
        }
        return this;
    }

    public Context getCurrentContext() {
        return getContext(config.getCurrent());
    }

    public Context getContext(String name) {
        for (Context c : config.getContexts()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public void printCurrentContext() {
        try {
            Output.println(objectMapper.writeValueAsString(getContext(config.getCurrent())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printContext(String name) {
        try {
            Output.println(objectMapper.writeValueAsString(getContext(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printContexts() {
        try {
            Output.println(objectMapper.writeValueAsString(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
