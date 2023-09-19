#!/bin/bash -e

./oc-login-dev.sh

oc delete project keycloak
