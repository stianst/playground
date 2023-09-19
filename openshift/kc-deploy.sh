#!/bin/bash -e

./oc-login-dev.sh

oc new-project keycloak

oc process -f https://raw.githubusercontent.com/keycloak/keycloak-quickstarts/latest/openshift/keycloak.yaml \
    -p KEYCLOAK_ADMIN=admin \
    -p KEYCLOAK_ADMIN_PASSWORD=admin \
    -p NAMESPACE=keycloak \
| oc create -f -

while ( ! oc get route keycloak ); do
    echo "Waiting for route"
    sleep 1
done

KEYCLOAK_URL=https://$(oc get route keycloak --template='{{ .spec.host }}') &&
echo "" &&
echo "Keycloak:                 $KEYCLOAK_URL" &&
echo "Keycloak Admin Console:   $KEYCLOAK_URL/admin" &&
echo "Keycloak Account Console: $KEYCLOAK_URL/realms/myrealm/account" &&
echo ""
