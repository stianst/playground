#!/bin/bash -e

source conf/env

cd $KEYCLOAK_HOME/bin

./kcadm.sh config credentials --server https://$KEYCLOAK_DOMAIN --realm master --user $KEYCLOAK_ADMIN --password $KEYCLOAK_ADMIN_PASSWORD

./kcadm.sh delete realms/$REALM_NAME
