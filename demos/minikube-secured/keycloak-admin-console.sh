#!/bin/bash -e

source conf/env

echo "Keycloak admin URL: $KEYCLOAK_URL/admin"
echo "Keycloak admin username: $KEYCLOAK_ADMIN"
echo "Keycloak admin password: $KEYCLOAK_ADMIN_PASSWORD"
echo ""

xdg-open $KEYCLOAK_URL/admin