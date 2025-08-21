#!/bin/bash

curl -X POST \
  -d grant_type=client_credentials \
  -d client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer \
  -d client-assertion-type=jwt-bearer \
  -d client_assertion="$1" \
  http://localhost:8080/realms/spiffe/protocol/openid-connect/token