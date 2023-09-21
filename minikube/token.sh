#!/bin/bash -e

REALM=kube
CLIENT=kubectl
SECRET=Z7xBtgLiLBdAVnQsApQWUhRyvLSRj2lb
USER=st
PASS=st

RESULT=$(curl -s --data "client_id=$CLIENT&client_secret=$SECRET&username=$USER&password=$PASS&grant_type=password&scope=openid" http://localhost:8080/realms/$REALM/protocol/openid-connect/token)

REFRESH_TOKEN=$(echo $RESULT | jq -r '.refresh_token')
ID_TOKEN=$(echo $RESULT | jq -r '.id_token')
ACCESS_TOKEN=$(echo $RESULT | jq -r '.access_token')

echo "Refresh Token"
echo $REFRESH_TOKEN
echo ""
echo "ID Token"
echo $ID_TOKEN
echo ""
echo "Access Token"
echo $ACCESS_TOKEN
echo ""
