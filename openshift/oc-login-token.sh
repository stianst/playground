#!/bin/bash -e

API_SERVER_URL=$(crc console --credentials | grep developer | cut -f 2 -d "'" | cut -f 7 -d ' ')
KC_URL="https://$(oc get route -n keycloak keycloak --template='{{ .spec.host }}')/realms/master"

echo $API_SERVER_URL

ID_TOKEN=$(curl -q -k "$KC_URL/protocol/openid-connect/token" -d "grant_type=password" -d "client_id=admin-cli" -d "scope=openid" -d "username=admin" -d "password=admin" | jq -r '.id_token')

echo $ID_TOKEN

oc get pods --token="$ID_TOKEN"