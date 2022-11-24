#!/bin/bash

# When a PR is created or last updated the head.sha is updated for the PR, and there is new check suites and check runs
# created.
#
# For each commit (head.sha of a PR) there is one check suite per workflow, and one check run per job. These are created
# very quickly after the PR is created or updated.
#
# A check suite has:
#
# * created_at - When the check suite was created
# * updated_at - When the check suite was last updated, which seems to be when all check runs complete. Unsure if there
#                are other conditions that can update a check suite, but doesn't seem like there is.
#
# A check run has:
#
# * started_at   - If a check run is queued this will be the time the check run was created, but when the check run starts
#                  the started_at is updated, so for a completed check run the started_at will be when the check run started
#                  running.
# * completed_at - The time the check run was finished
#
# There is no direct way to see when a PR was created, but that can be inferred from when the check suites was created.
#
# Best way to decide the time from a PR was created or last updated until all workflows and jobs completed seems to be:
#
# (max updated_at for check suites) - (min created_at for check suites)
#
# This will include the running time, the waiting time if there are queues, and the waiting time for manual re-running
# failed jobs.

REPO="keycloak/keycloak"
DAYS=7

while getopts "d:f:t:p:r:?" arg; do
    case $arg in
        d)
            DAYS=$OPTARG
            ;;
        f)
            FROM=$OPTARG
            ;;
        t)
            TO=$OPTARG
            ;;
        p)
            PR=$OPTARG
            ;;
        r)
            REPO=$OPTARG
            ;;
        *)
            echo "options:"
            echo "  -r <repo>      Repository (default keycloak/keycloak)"
            echo "  -d <days>      Number of days to fetch (default 7)"
            echo "  -f <date>      Fetch PRs merged from date (YYYY-MM-DD)"
            echo "  -t <date>      Fetch PRs merged to date (YYYY-MM-DD)"
            echo "  -p <num>       Show statistics for a single PR"
            exit 1
            ;;
    esac
done

DAYS=$((DAYS-1))
if [ "$FROM" == "" ]; then
  TO=`date +%Y-%m-%d`
  FROM=`date -d "-$DAYS days" +%Y-%m-%d`
else
  if [ "$TO" == "" ]; then
    TO=`date -d "$FROM +$DAYS days" +%Y-%m-%d`
  fi
fi

pr_wait () {
    PR=$1

    PULL=`gh api repos/$REPO/pulls/$PR`

    LOGIN=`echo $PULL | jq -r '.user.login'`
    TITLE=`echo $PULL | jq -r '.title'`

    PULL_CREATED=`echo $PULL | jq -r '.created_at | fromdate'`
    HEAD_SHA=`echo $PULL | jq -r '.head.sha'`

    CHECK_SUITES=`gh api repos/$REPO/commits/$HEAD_SHA/check-suites`

    CHECK_SUITE_CREATED=`echo $CHECK_SUITES | jq -r '[.check_suites[].created_at] | min | fromdate'`
    CHECK_SUITE_UPDATED=`echo $CHECK_SUITES | jq -r '[.check_suites[].updated_at] | max | fromdate'`

    # CHECK_RUNS=`gh api repos/$REPO/commits/$HEAD_SHA/check-runs`
    # CHECK_RUN_COMPLETED=`echo $CHECK_RUNS | jq -r '[.check_runs[].completed_at] | max | fromdate'`

    TOTAL_TIME=`echo "($CHECK_SUITE_UPDATED - $CHECK_SUITE_CREATED) / 60" | bc`

    echo -e "https://github.com/$REPO/pull/$PR\t" \
      "$TITLE\t" \
      "$LOGIN\t" \
      "$TOTAL_TIME"
}

pr_list () {
  # Only count PRs that are merged, as we know failed tests have been re-run if applicable. We're also discounting PRs
  # from external contributors as we want to focus on waiting time for ourselves initially, and some PRs from the
  # community require manual approving of the run first.
  gh api -X GET search/issues --paginate -F q="repo:$REPO is:pr is:merged closed:$FROM..$TO" -q '.items.[] | select(.author_association == "MEMBER") | .number'
}

if [ "$PR" != "" ]; then
    echo -e "PR\tTitle\tLogin\tTime"
    pr_wait $PR
else
    echo "Fetching PRs merged from $FROM to $TO"
    echo ""
    echo -e "PR\tTitle\tLogin\tTime"
    for i in `pr_list`; do
        pr_wait $i
    done
fi
