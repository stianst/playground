#/bin/bash -e

WORKFLOW=$1
DATE=$2

if [ ! -d logs ]; then
    mkdir logs
fi

for i in $(gh run list -L 100 -R stianst/keycloak -w $WORKFLOW -s failure --created $DATE --json databaseId | jq -r .[].databaseId); do
    echo $i
    if [ ! -f logs/$i ]; then
        gh run -R stianst/keycloak view --log-failed $i > logs/$i
    fi
done

