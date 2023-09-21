#!/bin/bash -e

source conf/env

cd $KEYCLOAK_HOME/bin

./kcadm.sh config credentials --server https://$KEYCLOAK_DOMAIN --realm master --user $KEYCLOAK_ADMIN --password $KEYCLOAK_ADMIN_PASSWORD

./kcadm.sh create realms -s realm=$REALM_NAME -s enabled=true

./kcadm.sh create groups -r $REALM_NAME -s name=$GROUP_NAME

./kcadm.sh create users -r $REALM_NAME -s username=$USER_NAME -s email=$USER_EMAIL -s emailVerified=true -s enabled=true
./kcadm.sh set-password -r $REALM_NAME --username $USER_NAME --new-password $USER_PASSWORD

USER_ID=$(./kcadm.sh get users -r $REALM_NAME -q username=$USER_NAME --fields id --format csv --noquotes)
GROUP_ID=$(./kcadm.sh get -r $REALM_NAME groups -q q=$GROUP_NAME -q exact=true --fields id --format csv --noquotes)

./kcadm.sh update -r $REALM_NAME users/$USER_ID/groups/$GROUP_ID

./kcadm.sh create clients -r $REALM_NAME \
    -s clientId=$CLIENT_ID \
    -s enabled=true \
    -s clientAuthenticatorType=client-secret \
    -s secret=$CLIENT_SECRET \
    -s standardFlowEnabled=true \
    -s directAccessGrantsEnabled=true \
    -s 'redirectUris=["*"]'

CLIENT_UUID=$(./kcadm.sh get clients -r $REALM_NAME -q clientId=$CLIENT_ID --fields id --format csv --noquotes)

./kcadm.sh create -r $REALM_NAME clients/$CLIENT_UUID/protocol-mappers/models -b '{"protocol":"openid-connect","protocolMapper":"oidc-group-membership-mapper","name":"groups","config":{"claim.name":"groups","full.path":"false","id.token.claim":"true","access.token.claim":"true","userinfo.token.claim":"true"}}'
