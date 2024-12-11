#!/bin/bash -e

SINCE=$(date --date='7 day ago' +%Y-%m-%d)

echo "Retried"

gh run list --created ">$SINCE"  -L 30 -R keycloak/keycloak-private -w js-ci.yml --json attempt,url,conclusion | jq '.[] | [.attempt, .url, .conclusion] | @tsv' -r | grep -v '^1'

