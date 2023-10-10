#!/bin/bash

source conf/env

cd $KEYCLOAK_HOME/bin

echo "Login to kcadm"
echo "--------------"
./kcadm.sh config credentials --server https://$KEYCLOAK_DOMAIN --realm master --user $KEYCLOAK_ADMIN --password $KEYCLOAK_ADMIN_PASSWORD
echo ""

echo "Creating realm $REALM_NAME"
echo "--------------------------"
./kcadm.sh create realms -s realm=$REALM_NAME -s enabled=true
echo ""

echo "Creating group $GROUP_NAME"
echo "--------------------------"
./kcadm.sh create groups -r $REALM_NAME -s name=$GROUP_NAME
echo ""

echo "Creating user $USER_NAME"
echo "------------------------"
./kcadm.sh create users -r $REALM_NAME -s username=$USER_NAME -s email=$USER_EMAIL -s emailVerified=true -s enabled=true -s groups="[\"/$GROUP_NAME\"]"
./kcadm.sh set-password -r $REALM_NAME --username $USER_NAME --new-password $USER_PASSWORD
echo ""

echo "Creating kube admin $KUBE_ADMIN"
echo "-------------------------------"
./kcadm.sh create users -r $REALM_NAME -s username=$KUBE_ADMIN -s email=$KUBE_ADMIN_EMAIL -s emailVerified=true -s enabled=true
./kcadm.sh set-password -r $REALM_NAME --username $KUBE_ADMIN --new-password $KUBE_ADMIN_PASSWORD
echo ""

echo "Creating $KUBE_CLIENT_ID for minikube"
echo "-------------------------------------"
./kcadm.sh create clients -r $REALM_NAME -f - << EOF
{
  "clientId": "$KUBE_CLIENT_ID",
  "protocol": "openid-connect",
  "fullScopeAllowed": false
}
EOF
echo ""

echo "Creating $KUBECTL_CLIENT_ID for kubectl"
echo "---------------------------------------"
./kcadm.sh create clients -r $REALM_NAME -f - << EOF
{
  "clientId": "$KUBECTL_CLIENT_ID",
  "protocol": "openid-connect",
  "enabled": true,
  "publicClient": true,
  "directAccessGrantsEnabled": true,
  "standardFlowEnabled": true,
  "fullScopeAllowed": false,
  "attributes": {
    "oauth2.device.authorization.grant.enabled": true
  },
  "redirectUris": [
    "http://127.0.0.1/callback"
  ],
  "protocolMappers": [
    {
      "protocol": "openid-connect",
      "protocolMapper": "oidc-audience-mapper",
      "name": "aud",
      "config": {
        "included.client.audience": "$KUBE_CLIENT_ID",
        "id.token.claim": "true",
        "access.token.claim": "true"
      }
    },
    {
      "protocol": "openid-connect",
      "protocolMapper": "oidc-group-membership-mapper",
      "name": "groups",
      "config":
      {
        "claim.name": "groups",
        "full.path": "false",
        "id.token.claim": "true",
        "access.token.claim":"true",
        "userinfo.token.claim":"true"
      }
    }
  ]
}
EOF
echo ""