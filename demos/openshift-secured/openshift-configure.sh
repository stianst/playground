#!/bin/bash

source conf/env

ISSUER_URL="https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME"

WELL_KNOWN_FILE=$(mktemp)

curl --silent "$ISSUER_URL/.well-known/openid-configuration" -o $WELL_KNOWN_FILE

OC_CONFIG='{
  "spec": {
    "oauthMetadata": {
      "name": "oauth-meta"
    },
    "type": "None"
  }
}'

oc create configmap oauth-meta --from-file oauthMetadata=$WELL_KNOWN_FILE -n openshift-config
oc patch authentication cluster -p "$OC_CONFIG" --type=merge

KUBE_CONFIG='{
  "spec": {
    "unsupportedConfigOverrides": {
      "apiServerArguments": {
        "oidc-client-id": ["'$OPENSHIFT_CLIENT_ID'"],
        "oidc-issuer-url": ["'$ISSUER_URL'"],
        "oidc-username-claim": ["email"],
        "oidc-groups-claim": ["groups"]
      }
    }
  }
}'

oc patch kubeapiserver cluster -p "$KUBE_CONFIG" --type=merge