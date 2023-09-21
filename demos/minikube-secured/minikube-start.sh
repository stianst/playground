#!/bin/bash -e

source conf/env

minikube start \
    --extra-config=apiserver.authorization-mode=RBAC \
    --extra-config=apiserver.oidc-issuer-url=https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME \
    --extra-config=apiserver.oidc-username-claim=email \
    --extra-config=apiserver.oidc-groups-claim=groups \
    --extra-config=apiserver.oidc-client-id=$CLIENT_ID
