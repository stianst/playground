#!/bin/bash

source conf/env

kubectl config set-credentials $USER_NAME \
   --auth-provider=oidc \
   --auth-provider-arg=idp-issuer-url=https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME \
   --auth-provider-arg=client-id=$CLIENT_ID \
   --auth-provider-arg=client-secret=$CLIENT_SECRET \
   --auth-provider-arg=refresh-token=$(./token.sh refresh)

kubectl config set-context test-oidc --cluster=minikube --user $USER_NAME
kubectl config use-context test-oidc
