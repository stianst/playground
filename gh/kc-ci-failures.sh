#!/bin/bash

DAYS=1
EVENTS=schedule
while getopts "hud:f:t:x:w:e:?" arg; do
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
        f)
            FROM=$OPTARG
            ;;
        t)
            TO=$OPTARG
            ;;
        x)
            TEST=$OPTARG
            ;;
        w)
            WORKFLOW=$OPTARG
            ;;
        e)
            EVENTS=$OPTARG
            ;;
        *)
            echo "options:"
            echo "  -h             Show headings"
            echo "  -u             Summary of results with number of occurrences (only works without headings)"
            echo "  -d <days>      Number of days to fetch (default 1)"
            echo "  -f <date>      From date (YYYY-MM-DD)"
            echo "  -t <date>      To date (YYYY-MM-DD)"
            echo "  -x <test>      Display details of a specific test"
            echo "  -w <workflow>  Display details of a specific workflow"
            echo "  -e <events>    Comma separated list of events"
            exit 1
            ;;
    esac
done

DIR=`dirname $0 | xargs readlink -f`
LOGS=$DIR/gh-logs

DAYS=$((DAYS-1))
if [ "$FROM" == "" ]; then
  TO=`date +%Y-%m-%d`
  FROM=`date -d "-$DAYS days" +%Y-%m-%d`
else
  if [ "$TO" == "" ]; then
    TO=`date -d "$FROM +$DAYS days" +%Y-%m-%d`
  fi
fi

echo "From $FROM to $TO"

FAILURES=`gh api -X GET repos/keycloak/keycloak/actions/workflows/ci.yml/runs -f status=failure -f event=$EVENTS -f created=$FROM..$TO --jq .workflow_runs[].id`

test_details() {
    echo "============================================================================================================================================"
    echo "Finding details for $TEST"
    echo "============================================================================================================================================"
    for RUN in $FAILURES; do
        LOGFILE=$LOGS/gh-actions-log-failed-$RUN
        ERROR=`cat $LOGFILE | grep "\[ERROR]   $TEST"`
        if [ "$ERROR" != "" ]; then
            echo "https://github.com/keycloak/keycloak/actions/runs/$RUN"
#            echo "$LOGFILE"
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

rewrite_test () {
    cat \
    | sed 's/[A-Za-z0-9]* » Runtime Arquillian initialization has already been attempted, but failed. See previous exceptions for cause/Runtime Arquillian initialization has already been attempted, but failed/g'
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
            cat $LOGFILE | grep "^$JOB" | grep -o '\[ERROR]   [A-Za-z0-9]*Test.*' | sed 's/\[ERROR]   / [TESTS] /g' | cut -d ':' -f 1 | rewrite_test
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

