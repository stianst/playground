#!/bin/bash

gh run -s queued -R cncf/keycloak-testing list -L 100 --json databaseId  | jq .[].databaseId -r | xargs -I {} echo gh run -R cncf/keycloak-testing cancel {}

gh run -s in_progress -R cncf/keycloak-testing list -L 100 --json databaseId  | jq .[].databaseId -r | xargs -I {} echo gh run -R cncf/keycloak-testing cancel {}