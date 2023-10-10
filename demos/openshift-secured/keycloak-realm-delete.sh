#!/bin/bash -e

source conf/env

cd $KEYCLOAK_HOME/bin

echo "Login to kcadm"
echo "--------------"
./kcadm.sh config credentials --server https://$KEYCLOAK_DOMAIN --realm master --user $KEYCLOAK_ADMIN --password $KEYCLOAK_ADMIN_PASSWORD
echo ""

echo "Deleting realm $REALM_NAME"
echo "--------------------------"
./kcadm.sh delete realms/$REALM_NAME
echo ""
