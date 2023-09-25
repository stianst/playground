#!/bin/bash

source conf/env

kc-oidc config set --context minikube \
    --issuer="https://$KEYCLOAK_DOMAIN/realms/myrealm" \
    --client-id="$CLIENT_ID" \
    --client-secret="$CLIENT_SECRET" \
    --user="$USER_NAME" \
    --user-password="$USER_PASSWORD" \
    --flow="resource-owner"

kc-oidc config use --context minikube

kubectl config set-context test-kc-oidc --cluster=minikube
kubectl config use-context test-kc-oidc

