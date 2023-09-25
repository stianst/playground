package org.keycloak.cli.oidc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.keycloak.cli.oidc.Output;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

public class ConfigHandler {

    private static ConfigHandler configHandler;

    private File configFile;
    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    private Config config;

    private ConfigHandler() throws ConfigException {
        File userHome = new File(System.getProperty("user.home"));
        File homeDir = new File(userHome,".kc");
        if (!homeDir.isDirectory()) {
            homeDir.mkdir();
        }
        configFile = new File(homeDir, "oidc.yaml");
        load();
    }

    public static ConfigHandler get() throws ConfigException {
        if (configHandler == null) {
            configHandler = new ConfigHandler();
        }
        return configHandler;
    }

    private void load() throws ConfigException {
        try {
            if (configFile.isFile()) {
                config = objectMapper.readValue(configFile, Config.class);
            }

            if (config == null) {
                config = new Config();
            }
        } catch (IOException e) {
            throw new ConfigException("Failed to load config", e);
        }
    }

    public ConfigHandler save() throws ConfigException {
        try {
            config.getContexts().sort(Comparator.comparing(Context::getName));

            if (config.getCurrent() == null && config.getContexts().size() == 1) {
                config.setCurrent(config.getContexts().get(0).getName());
            }

            objectMapper.writeValue(configFile, config);
        } catch (IOException e) {
            throw new ConfigException("Failed to save config", e);
        }
        return this;
    }

    public ConfigHandler setCurrent(String name) throws ConfigException {
        if (getContext(name) == null) {
            throw new ConfigException("Context not found");
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

    public void printCurrentContext() throws ConfigException {
        try {
            Output.println(objectMapper.writeValueAsString(getContext(config.getCurrent())));
        } catch (IOException e) {
            throw new ConfigException("Failed to serialize config");
        }
    }

    public void printContext(String name, boolean brief) throws ConfigException {
        Context context = getContext(name);
        if (brief) {
            context = briefCopy(context);
        }
        try {
            Output.println(objectMapper.writeValueAsString(context));
        } catch (IOException e) {
            throw new ConfigException("Failed to serialize config");
        }
    }

    public void printContexts(boolean brief) throws ConfigException {
        Config config = this.config;
        if (brief) {
            config = briefCopy(config);
        }
        try {
            Output.println(objectMapper.writeValueAsString(config));
        } catch (IOException e) {
            throw new ConfigException("Failed to serialize config");
        }
    }

    private Config briefCopy(Config config) {
        Config copy = new Config();
        copy.setCurrent(config.getCurrent());
        for (Context c : config.getContexts()) {
            copy.getContexts().add(briefCopy(c));
        }
        return copy;
    }

    private Context briefCopy(Context context) {
        Context copy = new Context();
        copy.setName(context.getName());
        copy.setIssuer(context.getIssuer());
        copy.setFlow(context.getFlow());
        return copy;
    }

}
