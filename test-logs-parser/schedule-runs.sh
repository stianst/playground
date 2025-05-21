#!/bin/bash -e

USER=$1
BRANCH=$2
WORKFLOW=$3

for i in $(seq 1 10); do
    echo $i 
    gh workflow run -R $USER/keycloak -r $BRANCH $WORKFLOW
done
