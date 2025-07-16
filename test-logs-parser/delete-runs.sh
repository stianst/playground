#!/bin/bash

gh run list -L 100 --json databaseId -R stianst/keycloak | jq -r .[].databaseId | xargs -I {} gh run delete -R stianst/keycloak {}