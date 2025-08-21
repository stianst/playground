#!/bin/bash

BASIC=$(echo -n "$1:$2" | base64)

curl -X POST \
  -d grant_type=client_credentials \
  -H "Authorization: Basic $BASIC" \
  http://localhost:8080/realms/spiffe/protocol/openid-connect/token