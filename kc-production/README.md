# Setting up a production-like deployment of Keycloak

Example for setting up a production-like deployment of Keycloak with containers.

## Load balancer

The load balancer binds to host network, and load balances to http://localhost:8080 and http://localhost:8081.

Run the load balancer with:

```
nginx-proxy/start.sh
```

## First let's download Keycloak (nightly)

```
wget -c https://github.com/keycloak/keycloak/releases/download/nightly/keycloak-999-SNAPSHOT.tar.gz -O - | tar -xz
```

## Next, let's install the hostname provider

```
mvn -f ../../kc-ext/hostname-debug/ clean install
cp ../../kc-ext/hostname-debug/target/hostname-debug.jar providers/
```

## Let's test hostname config

```
bin/kc.sh start --hostname-url=http://localhost --http-enabled=false --proxy=edge
bin/kc.sh start --hostname-url=http://localhost --http-enabled=false --proxy=edge --http-port=8081
```