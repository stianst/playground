#!/bin/bash -e

for i in $(gh run -R cncf/keycloak-testing list -L 100 --json databaseId | jq .[].databaseId -r); do
  gh run delete -R cncf/keycloak-testing $i
done