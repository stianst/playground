# Setup

Run PostgreSQL as a container with the following command:

    sudo podman run -d --name postgres -e POSTGRES_DB=keycloak -e POSTGRES_USER=keycloak -e POSTGRES_PASSWORD=password postgres

Install Keycloak somewhere then run ../kc-tools/db-poststgres.cli to configure PostgreSQL datasource (had a script to install module, but lost it :/). Then copy to kc-1, kc-2 and kc-3.

Run create-lb.sh and run lb/bin/standalone.sh.

Start KC nodes with 

   ./start-kc 1
   ./start-kc 2
   ./start-kc 3
   
To capture heap, stack, etc. during a run you can use `capture-debug-info.sh`.
