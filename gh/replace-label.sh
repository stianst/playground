#!/bin/bash -e

REPO=$1
OLD=$2
NEW=$3

function checkVar() {
  if [ "$1" == "" ]; then
    echo "usage: repository old-label new-label"
    exit 1
  fi  
}

checkVar "$REPO"
checkVar "$OLD"
checkVar "$NEW"

echo "Changing $OLD to $NEW in $REPO"
echo "------------------"

for i in $(gh issue list -R "$REPO" -s all -l "$OLD" --json number | jq -r .[].number); do
  gh issue edit -R "$REPO" $i --remove-label "$OLD" --add-label "$NEW"
done

