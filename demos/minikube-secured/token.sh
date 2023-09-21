#!/bin/bash -e

source conf/env

RESULT=$(curl -s --data "client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&username=$USER_NAME&password=$USER_PASSWORD&grant_type=password&scope=openid" https://$KEYCLOAK_DOMAIN/realms/$REALM_NAME/protocol/openid-connect/token)

REFRESH_TOKEN=$(echo $RESULT | jq -r '.refresh_token')
ID_TOKEN=$(echo $RESULT | jq -r '.id_token')
ACCESS_TOKEN=$(echo $RESULT | jq -r '.access_token')

if [ "$1" == "refresh" ]; then
  echo $REFRESH_TOKEN
elif [ "$1" == "id" ]; then
  echo $ID_TOKEN
elif [ "$1" == "access" ]; then
  echo $ACCESS_TOKEN
elif [ "$1" == "debug" ]; then
  echo "=================================================================================="
  echo "Response"
  echo "=================================================================================="
  echo $RESULT | jq
  echo "=================================================================================="
  echo "ID Token"
  echo "=================================================================================="
  base64url decode $(echo $ID_TOKEN | cut -d . -f 2) | jq
  echo "=================================================================================="
  echo "Access Token"
  echo "=================================================================================="
  base64url decode $(echo $ACCESS_TOKEN | cut -d . -f 2) | jq
fi
