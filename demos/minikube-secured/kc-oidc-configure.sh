#!/bin/bash -e

source conf/env

kc-oidc config set --context=minikube-demo-user \
  --issuer=$KEYCLOAK_URL/realms/$REALM_NAME \
  --client-id=$KUBECTL_CLIENT_ID \
  --user=$USER_NAME \
  --user-password=$USER_PASSWORD \
  --flow=resource-owner

kc-oidc config set --context=minikube-demo-admin \
  --issuer=$KEYCLOAK_URL/realms/$REALM_NAME \
  --client-id=$KUBECTL_CLIENT_ID \
  --user=$KUBE_ADMIN \
  --user-password=$KUBE_ADMIN_PASSWORD \
  --flow=resource-owner
