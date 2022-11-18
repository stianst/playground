#!/bin/bash -e

DAYS=7
while getopts "d:?" arg; do
    case $arg in
        d)
            DAYS=$OPTARG
            ;;
        *)
            echo "options:"
            echo "  -d <days>      Number of days to fetch (default 7)"
            exit 1
            ;;
    esac
done

WORKFLOW='Keycloak CI'

WORKFLOWS=`gh workflow list -R keycloak/keycloak`

KEYCLOAK_CI=1507662
KEYCLOAK_OPERATOR_CI=17282405
CYPRESS_CI=4783142

DATE=`date -d "-$DAYS days" +%Y-%m-%d`

workflowstatus() {
    REPO=$1
    WORKFLOW=$2
    WORKFLOW_ID=$3
    
    CONCLUSIONS=`gh api -X GET repos/keycloak/$REPO/actions/workflows/$WORKFLOW_ID/runs -F event=schedule -F created=">$DATE" --jq .workflow_runs.[].conclusion`
    
    TOTAL=`echo $CONCLUSIONS | sed 's/ /\n/g' | wc -l`
    SUCCESS=`echo $CONCLUSIONS | sed 's/ /\n/g' | grep success | wc -l`
    FAILURE=`echo $CONCLUSIONS | sed 's/ /\n/g' | grep failure | wc -l`
    CANCELLED=`echo $CONCLUSIONS | sed 's/ /\n/g' | grep cancelled | wc -l`

    SUCCESS_RATE=$(( $SUCCESS*100/TOTAL ))

    echo -e "$WORKFLOW\t\t$SUCCESS_RATE\t\t$SUCCESS\t$FAILURE\t$CANCELLED"
}


echo -e "Workflow\t\tSucces %\tSuccess\tFailure\tCancelled"

workflowstatus keycloak 'Keycloak CI' $KEYCLOAK_CI
workflowstatus keycloak 'Operator CI' $KEYCLOAK_OPERATOR_CI
workflowstatus keycloak-ui 'UI Cypress CI' $CYPRESS_CI

