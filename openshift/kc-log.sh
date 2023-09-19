#!/bin/bash -e

./oc-login-dev.sh

POD=$(oc get pods | grep 'keycloak' | grep -v 'deploy' | cut -f 1 -d ' ')

oc logs -f $POD
