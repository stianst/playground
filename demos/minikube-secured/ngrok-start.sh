#!/bin/bash -e

source conf/env

mkdir -p log
if [ -f log/ngrok.log ]; then
  rm log/ngrok.log
fi

ngrok http --domain=$KEYCLOAK_DOMAIN 8080 --log log/ngrok.log
