#!/bin/bash -e

if [ "$1" == "" ]; then
  REPO="keycloak/keycloak"
else
  REPO="$1"
fi

FILE_NAME=stars-$(basename $REPO)

PAGES=$( gh api -X GET -F per_page=100 /repos/$REPO/stargazers -i | grep 'Link:' | cut -d '=' -f 6 | cut -d '>' -f 1 )

echo "Repo: $REPO, total pages: $PAGES"

if [ -f $FILE_NAME ]; then
    CURRENT_NUM=$( cat $FILE_NAME | wc -l )
    CURRENT_PAGE=$(( $CURRENT_NUM / 100 ))
    cat $FILE_NAME | grep -v "^$PAGES," > /tmp/ghstars
else
    CURRENT_PAGE=1
fi

for i in $( seq $CURRENT_PAGE $PAGES); do
    echo "Fetching page $i"
    gh api -F page=$i -X GET -F per_page=100 /repos/$REPO/stargazers -H Accept:application/vnd.github.star+json -q .[].starred_at | xargs -I {} echo $i,{} >> /tmp/ghstars
done

mv /tmp/ghstars $FILE_NAME
