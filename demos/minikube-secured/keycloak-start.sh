#!/bin/bash -e

source conf/env

export KEYCLOAK_ADMIN=$KEYCLOAK_ADMIN
export KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD

$KEYCLOAK_HOME/bin/kc.sh start-dev --hostname-url=https://$KEYCLOAK_DOMAIN --hostname-admin-url=https://$KEYCLOAK_DOMAIN