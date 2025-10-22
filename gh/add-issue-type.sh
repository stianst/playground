#!/bin/bash -e

REPO=$1

function checkVar() {
  if [ "$1" == "" ]; then
    echo "usage: repository"
    exit 1
  fi  
}

checkVar "$REPO"

echo "Changing $OLD to $NEW in $REPO"
echo "------------------"

for i in $(gh issue list -R "$REPO" -L 500 -S "state:open no:type" --json number,labels | jq '[.[] | { number, labels: .labels | map(.name) | [.[] | select(startswith("kind/")) ] | join(",") }]' | jq -r '.[] | [.number, .labels] | join(",")'); do
  ISSUE=$(echo $i | cut -d ',' -f 1)
  TYPE=$(echo $i | cut -d ',' -f 2 | sed 's|kind/||')

  if [ "$TYPE" == "epic" ]; then
    echo "https://github.com/keycloak/keycloak/issues/$ISSUE --> $TYPE (skipping)"
  elif [ "$TYPE" == "" ]; then
    echo "https://github.com/keycloak/keycloak/issues/$ISSUE --> $TYPE (skipping)"
  else
    echo "https://github.com/keycloak/keycloak/issues/$ISSUE --> $TYPE"
    echo "{\"type\":\"$TYPE\"}" | gh api -X PATCH "repos/$REPO/issues/$ISSUE" --silent --input -
  fi


done

