#!/bin/bash -e

if [ "$1" == "" ]; then
  REPO="keycloak/keycloak"
else
  REPO="$1"
fi

FILE_NAME=stars-$(basename $REPO)

cat $FILE_NAME | cut -d ',' -f 2 | sort | awk -F '-' '{ print $1"-"$2 }' | uniq -c
