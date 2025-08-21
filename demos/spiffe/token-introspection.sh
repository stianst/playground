#!/bin/bash -e

SPIFFE_JWT_SVID=$(./fetch-jwt-svid.sh)
ACCESS_TOKEN=$(./client-credential-grant.sh | jq -r .access_token)

curl -s -X POST \
  -d token=$ACCESS_TOKEN \
  -d client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer \
  -d client-assertion-type=jwt-bearer \
  -d client_assertion="$SPIFFE_JWT_SVID" \
  http://localhost:8080/realms/spiffe/protocol/openid-connect/token/introspect | jq