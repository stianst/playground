#!/bin/bash -e

TOKEN=$(./retrieve-jwt-svid.sh)

kct decode $TOKEN --jwks=http://localhost:8082/keys