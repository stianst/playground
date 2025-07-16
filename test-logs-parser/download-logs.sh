#!/bin/bash -e

USER=$1
WORKFLOW=$2

if [ "$3" == "" ]; then
  DATE=$(date -Idate)
else
  DATE=$3
fi

if [ ! -d logs ]; then
    mkdir logs
fi

for i in $(gh run list -L 100 -R stianst/keycloak -w $WORKFLOW -s failure --created $DATE --json databaseId | jq -r .[].databaseId); do
    echo $i
    if [ ! -f logs/$i ]; then
        gh run -R stianst/keycloak view --log-failed $i > logs/$i
    fi
    if [ ! -f logs/$i.json ]; then
        gh run -R stianst/keycloak view --json name,conclusion,databaseId,jobs $i > logs/$i.json
    fi
done

