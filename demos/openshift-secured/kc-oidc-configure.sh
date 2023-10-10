#!/bin/bash -e

source conf/env

kc-oidc config set --context=openshift-demo-user \
  --issuer=$KEYCLOAK_URL/realms/$REALM_NAME \
  --client-id=$OC_CLIENT_ID \
  --user=$USER_NAME \
  --user-password=$USER_PASSWORD \
  --flow=resource-owner

kc-oidc config set --context=openshift-demo-admin \
  --issuer=$KEYCLOAK_URL/realms/$REALM_NAME \
  --client-id=$OC_CLIENT_ID \
  --user=$OPENSHIFT_ADMIN \
  --user-password=$OPENSHIFT_ADMIN_PASSWORD \
  --flow=resource-owner
