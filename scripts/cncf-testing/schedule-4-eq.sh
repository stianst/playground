#!/bin/bash -e

for i in {1..4}; do
  echo -n "$i  "
  gh workflow run -R cncf/keycloak-testing ci.yml -r equinix
done
