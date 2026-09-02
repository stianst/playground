package com.example.keycloak.cedar;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CedarSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "cedar";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return CedarProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return CedarProviderFactory.class;
    }

}
