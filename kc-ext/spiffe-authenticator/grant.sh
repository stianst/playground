#!/bin/bash

curl -X POST http://localhost:8080/realms/master/protocol/openid-connect/token -H "Content-Tyoe: application/json" \
-d "grant_type=client_credentials" \
-d "client_assertion_type=spiffe-jwt-svid" \
-d "client_assertion=$1"
