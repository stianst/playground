#!/bin/bash

pr_wait () {
    PR=$1
    RES=`gh api repos/keycloak/keycloak/pulls/$PR -q '.head.sha, .user.login, (.created_at | fromdate),.author_association'`
    HASH=`echo $RES | cut -d ' ' -f 1`
    LOGIN=`echo $RES | cut -d ' ' -f 2`
    CREATED=`echo $RES | cut -d ' ' -f 3`
    ASSOCIATION=`echo $RES | cut -d ' ' -f 4`
    COMPLETED=`gh api repos/keycloak/keycloak/commits/$HASH/check-runs -q '.check_runs.[].completed_at | fromdate' | sort -r | head -n 1`
    WAIT=`echo "($COMPLETED - $CREATED) / 60" | bc`

    echo -e "https://github.com/keycloak/keycloak/pull/$PR\t$WAIT\t$TITLE\t$LOGIN\t$ASSOCIATION"
}

echo -e "PR\tWait (min)\tLogin\tAssociation"

if [ "$1" != "" ]; then
    pr_wait $1
else
    for i in `gh pr list -R keycloak/keycloak -s merged --json number -q .[].number`; do
        pr_wait $i
    done
fi
