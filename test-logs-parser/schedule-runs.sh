#!/bin/bash -e

BRANCH=$1
WORKFLOW=$2

for i in $(seq 1 100); do 
    echo $i 
    gh workflow run -R stianst/keycloak -r $BRANCH $WORKFLOW
done
