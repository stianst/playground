#!/bin/bash -e

./oc-login-kubeadmin.sh

KC_URL="https://$(oc get route -n keycloak keycloak --template='{{ .spec.host }}')/realms/master"

echo "Keycloak URL: $KC_URL"

# Got no clue what this does

#oc patch cm -n openshift-config-managed default-ingress-cert -p '{"metadata":{"namespace":"openshift-config"}}' --dry-run=client -o yaml | oc apply -f -
#oc patch proxy cluster -p '{"spec":{"trustedCA":{"name":"default-ingress-cert"}}}' --type=merge

WELL_KNOWN_FILE=$(mktemp)

curl -k "${KC_URL}/.well-known/openid-configuration" > $WELL_KNOWN_FILE

#oc create configmap oauth-meta --from-file $WELL_KNOWN_FILE -n openshift-config

#oc patch authentication cluster -p '{"spec":{"oauthMetadata":{"name":"oauth-meta"},"type":"None"}}' --type=merge

#oc patch kubeapiserver cluster -p '{"spec":{"unsupportedConfigOverrides":{"apiServerArguments":{"oidc-ca-file":["/etc/kubernetes/static-pod-certs/configmaps/trusted-ca-bundle/ca-bundle.crt"],"oidc-client-id":["admin-cli"], "oidc-issuer-url":["'"${KC_URL}"'"]}}}}' --type=merge
