#!/bin/bash -e

USER=$1
BRANCH=$2

if [ "$3" == "" ]; then
  DATE=$(date -Idate)
else
  DATE=$3
fi

gh api -X GET /repos/$USER/keycloak/actions/runs -F branch=$BRANCH -F per_page=100 --paginate -F created=$DATE | jq -r '.workflow_runs[] | [.status, .conclusion] | @csv' | sort | uniq -c