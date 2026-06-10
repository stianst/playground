#!/bin/bash -e

USER=$1
CURRENT_DATE=$(date +%F)
FROM_DATE=$(date -d "6 months ago" +%F)
LIMIT=1000
REPO="keycloak/keycloak"

FILE="gh-stats-$USER-$CURRENT_DATE.md"
rm $FILE

print() {
    echo "$1" | tee -a $FILE
}

print_header() {
    echo "| $1 | $2 |" | tee -a $FILE
    echo "| ---- | - |" | tee -a $FILE
}

print_row() {
    echo "| $1 | $2 |" | tee -a $FILE
}

fetch_discussions() {
    local query_type=$1
    local cursor=${2:-null}
    local has_next=true
    local total=0
    local discussions=()

    while [ "$has_next" = "true" ]; do
        if [ "$cursor" = "null" ]; then
            cursor_param="null"
        else
            cursor_param="\"$cursor\""
        fi

        # GraphQL query to search discussions
        result=$(gh api graphql -f query="
        query {
          search(query: \"repo:$REPO is:discussion $query_type:$USER created:>=$FROM_DATE\", type: DISCUSSION, first: 100, after: $cursor_param) {
            discussionCount
            pageInfo {
              hasNextPage
              endCursor
            }
            edges {
              node {
                ... on Discussion {
                  number
                  title
                  url
                  createdAt
                }
              }
            }
          }
        }
        ")

        # Extract values
        local count=$(echo "$result" | jq -r '.data.search.discussionCount')
        has_next=$(echo "$result" | jq -r '.data.search.pageInfo.hasNextPage')
        cursor=$(echo "$result" | jq -r '.data.search.pageInfo.endCursor')

        # Get discussions from this page
        local page_discussions=$(echo "$result" | jq -r '.data.search.edges[].node | "\(.number)|\(.title)|\(.url)"')

        if [ ! -z "$page_discussions" ]; then
            discussions+=("$page_discussions")
        fi

        if [ "$has_next" = "false" ]; then
            total=$count
            break
        fi
    done

    echo "$total"
}

print "# User: $USER"
print "From $FROM_DATE to $CURRENT_DATE"

print "## Statistics"

print_header "Stat" "#"

PRS_CREATED=$(gh search prs --author $USER --created ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --merged --json id --jq 'length')
print_row "PRs - Created" "$PRS_CREATED"

PRS_REVIEWED=$(gh search prs --reviewed-by $USER --created ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --merged --json id --jq 'length')
print_row "PRs - Reviewed" "$PRS_REVIEWED"

ISSUES_CREATED=$(gh search issues --author $USER --updated ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --json id --jq 'length')
print_row "Issues - Created" "$ISSUES_CREATED"

ISSUES_INVOLVED=$(gh search issues --involves $USER --updated ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --json id --jq 'length')
print_row "Issues - Involved" "$ISSUES_INVOLVED"

ISSUES_CLOSED=$(gh search issues --involves $USER --closed ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --json id --jq 'length')
print_row "Issues - Closed" "$ISSUES_CLOSED"

BUGS_INVOLVED=$(gh search issues --involves $USER --label "kind/bug" --updated ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --json id --jq 'length')
print_row "Bugs - Involved" "$BUGS_INVOLVED"

BUGS_CLOSED=$(gh search issues --assignee $USER --label "kind/bug" --closed ">=$FROM_DATE" --limit "$LIMIT" --repo "$REPO" --json id --jq 'length')
print_row "Bugs - Closed" "$BUGS_CLOSED"

DISCUSSIONS_CREATED=$(fetch_discussions "author")
print_row "Discussions - Created" "$DISCUSSIONS_CREATED"

DISCUSSIONS_INVOLVED=$(fetch_discussions "involves")
print_row "Discussions - Involved" "$DISCUSSIONS_INVOLVED"

print "## PRs"

print_header "#" "Title"
gh pr list -R $REPO --search "author:$USER created:>=2025-10-08 state:merged" --limit "$LIMIT" --json number,title,url | jq -r '.[] | "| [\(.number)](\(.url)) | \(.title) |"' | tee -a $FILE

glow $FILE

echo $FILE
