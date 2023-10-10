#!/bin/bash -e

source conf/env

echo "Starting minikube with configuration:"
echo "- apiserver.authorization-mode=RBAC"
echo "- apiserver.oidc-issuer-url=https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME"
echo "- apiserver.oidc-username-claim=email"
echo "- apiserver.oidc-groups-claim=groups"
echo "- apiserver.oidc-client-id=$KUBE_CLIENT_ID"
echo ""

minikube start \
    --extra-config=apiserver.authorization-mode=RBAC \
    --extra-config=apiserver.oidc-issuer-url=https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME \
    --extra-config=apiserver.oidc-username-claim=email \
    --extra-config=apiserver.oidc-groups-claim=groups \
    --extra-config=apiserver.oidc-client-id=$KUBE_CLIENT_ID
