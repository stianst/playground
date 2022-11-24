#!/bin/bash

DAYS=1
while getopts "hud:t:w:?" arg; do
    case $arg in
        h)
            HEADINGS=true
            ;;
        u)
            UNIQ=true
            ;;
        d)
            DAYS=$OPTARG
            ;;
        t)
            TEST=$OPTARG
            ;;
        w)
            WORKFLOW=$OPTARG
            ;;
        *)
            echo "options:"
            echo "  -h             Show headings"
            echo "  -u             Summary of results with number of occurrences (only works without headings)"
            echo "  -d <days>      Number of days to fetch (default 1)"
            echo "  -t <test>      Display details of a specific test"
            echo "  -w <workflow>  Display details of a specific workflow" 
            exit 1
            ;;
    esac
done

DIR=`dirname $0 | xargs readlink -f`
LOGS=$DIR/gh-logs

FROM_DATE=`date +%Y-%m-%d -d "$DAYS day ago"`

FAILURES=`gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs -f status=failure -f event=schedule -f created=">$FROM_DATE" --jq .workflow_runs[].id`

test_details() {
    echo "============================================================================================================================================"
    echo "Finding details for $TEST"
    echo "============================================================================================================================================"
    for RUN in $FAILURES; do
        LOGFILE=$LOGS/gh-actions-log-failed-$RUN
        ERROR=`cat $LOGFILE | grep "\[ERROR]   $TEST"`
        if [ "$ERROR" != "" ]; then
            echo "https://github.com/keycloak/keycloak/actions/runs/$RUN"
            echo "$LOGFILE"
            echo ""
            echo "$ERROR"
            echo "--------------------------------------------------------------------------------------------------------------------------------------------"
        fi
    done
}

workflow_details() {
    echo "============================================================================================================================================"
    echo "Finding details for $WORKFLOW"
    echo "============================================================================================================================================"
    for RUN in $FAILURES; do
        CONCLUSIONFILE=$LOGS/gh-actions-conclusion-$RUN
        ERROR=`cat $CONCLUSIONFILE | grep "$WORKFLOW" | grep -v '\[failure]' | grep -v '\[success]'`
        if [ "$ERROR" != "" ]; then
            echo "https://github.com/keycloak/keycloak/actions/runs/$RUN"
            echo "$CONCLUSIONFILE"
            echo ""
            echo "$ERROR"
            echo "--------------------------------------------------------------------------------------------------------------------------------------------"
        fi
    done
}

rewrite () {
    cat \
    | sed 's/Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7:test (default-test) on project /Test failures in /g' \
    | sed 's/There are test failures.//g' \
    | sed 's/://g' \
    | sed 's/.*Could not transfer artifact.*Connection timed out.*/Connection timed out transferring artifacts/g'
}

failures () {
    for RUN in $FAILURES; do
        LOGFILE=$LOGS/gh-actions-log-failed-$RUN
        if [ ! -f $LOGFILE ]; then
            gh run view -R keycloak/keycloak $RUN --log-failed > $LOGFILE
        fi
        CONCLUSIONFILE=$LOGS/gh-actions-conclusion-$RUN
        if [ ! -f $CONCLUSIONFILE ]; then
            gh run view -R keycloak/keycloak $RUN --json jobs --jq '.jobs[] | .name + ": [" + .conclusion + "]"' > $CONCLUSIONFILE
        fi

        if [ "$HEADINGS" = true ]; then 
            echo " "
            echo "============================================================================================================================================"
            echo "https://github.com/keycloak/keycloak/actions/runs/$RUN"
            echo "============================================================================================================================================"
        fi
        
        readarray -t JOBS < <(cat $LOGFILE | awk -F '\t' {' print $1 '} | sort | uniq)

            
        cat $CONCLUSIONFILE | grep -v '\[failure]' | grep -v '\[success]' | xargs -I {} echo " [ JOB ] {}"

        for JOB in "${JOBS[@]}"; do
            if [ "$HEADINGS" = true ]; then 
                echo "--------------------------------------------------------------------------------------------------------------------------------------------"
                echo "$JOB"
                echo "--------------------------------------------------------------------------------------------------------------------------------------------"
            fi
            
            cat $LOGFILE | grep "^$JOB" | grep -o 'Failed to execute goal.*' | rewrite | xargs -I {} echo " [BUILD] {}"
            cat $LOGFILE | grep "^$JOB" | grep -o '\[ERROR]   [A-Za-z0-9]*Test.*' | sed 's/\[ERROR]   / [TESTS] /g' | cut -d ':' -f 1
        done
    done
}

if [ "$TEST" != "" ]; then
    test_details
elif [ "$WORKFLOW" != "" ]; then 
    workflow_details
elif [ "$HEADINGS" = true ]; then 
    failures
elif [ "$UNIQ" = true ]; then
    failures | sort | uniq -c | sort -r -n
else
    failures | sort
fi

