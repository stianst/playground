#!/bin/bash

# Script to count discussions user stianst has been involved with in keycloak/keycloak

USER="stianst"
REPO_OWNER="keycloak"
REPO_NAME="keycloak"

echo "Fetching discussions for user $USER in $REPO_OWNER/$REPO_NAME..."
echo ""

# Calculate date 6 months ago
SIX_MONTHS_AGO=$(date -d "6 months ago" +%Y-%m-%d)
echo "Filtering discussions created after: $SIX_MONTHS_AGO"
echo ""

# Function to fetch discussions with pagination
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
          search(query: \"repo:$REPO_OWNER/$REPO_NAME is:discussion $query_type:$USER created:>$SIX_MONTHS_AGO\", type: DISCUSSION, first: 100, after: $cursor_param) {
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
    printf '%s\n' "${discussions[@]}"
}

# Get discussions where user is the author
echo "=== Discussions authored by $USER ==="
authored_output=$(fetch_discussions "author")
authored_count=$(echo "$authored_output" | head -n1)
echo "Total: $authored_count"
echo ""

# Get discussions where user is involved (commented, etc.)
echo "=== Discussions involving $USER (includes authored + participated) ==="
involved_output=$(fetch_discussions "involves")
involved_count=$(echo "$involved_output" | head -n1)
echo "Total: $involved_count"
echo ""

# Show some recent discussions
echo "=== Recent discussions involving $USER (showing up to 10) ==="
echo "$involved_output" | tail -n +2 | head -n10 | while IFS='|' read -r number title url; do
    echo "  #$number: $title"
    echo "    $url"
    echo ""
done

echo "Summary:"
echo "  Authored: $authored_count"
echo "  Total involved: $involved_count"
