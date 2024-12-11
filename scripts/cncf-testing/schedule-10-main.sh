#!/bin/bash -e

for i in {1..10}; do
  echo -n "$i  "
  gh workflow run -R cncf/keycloak-testing ci.yml -r main
done