#!/bin/bash -e

source conf/env

ngrok http --domain=$KEYCLOAK_DOMAIN 8080
