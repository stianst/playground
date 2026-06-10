#!/bin/bash -e

REPO="stianst/keycloak"

BRANCHES=($(gh api --cache 1h --paginate /repos/$REPO/branches -q '.[] | .name | select(startswith("release"))' | sort -V -r))
LATEST_REL_BRANCH=$(echo ${BRANCHES[0]} | sed 's|release/||') 

TAGS=($(gh api --cache 1h /repos/$REPO/tags --paginate -q ".[] | .name | select(startswith(\"$LATEST_REL_BRANCH\"))" | sort -V -r))

LATEST_TAG=$(echo ${TAGS[0]}) 

showAlerts() {
    echo "--------------------------------------------"
    echo "ref: $1"
    echo "--------------------------------------------"
    gh api -X GET --cache 1h --paginate '/repos/'$REPO'/code-scanning/alerts' -F state=open -F tool_name=Trivy -F ref="$1" | jq -r 'unique_by(.rule.id) | sort_by(.rule.id) | .[] | [ .created_at, .rule.id, .rule.description, .rule.security_severity_level ] | @tsv' | column -t -T 3 -s $'\t'
}

echo "============================================"
echo "Code scanning alerts for $REPO"
echo "============================================"

showAlerts refs/heads/main

for BRANCH in "${BRANCHES[@]}"; do
    showAlerts refs/heads/$BRANCH
done

showAlerts refs/tags/$LATEST_TAG
