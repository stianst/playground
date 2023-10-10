#!/bin/bash -e

source conf/env

mkdir -p log
if [ -f log/keycloak.log ]; then
  rm log/keycloak.log
fi

export KEYCLOAK_ADMIN=$KEYCLOAK_ADMIN
export KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD

$KEYCLOAK_HOME/bin/kc.sh start-dev --hostname-url=$KEYCLOAK_URL --hostname-admin-url=$KEYCLOAK_URL --log="console,file" --log-file="log/keycloak.log"