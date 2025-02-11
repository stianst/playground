#!/bin/bash -e

if [ "$1" == "" ]; then
  DATE=$(date -Idate)
else
  DATE=$1
fi

gh api -X GET /repos/stianst/keycloak/actions/runs --paginate -F per_page=100 -F created=$DATE --jq .workflow_runs[].status | sort | uniq -c
