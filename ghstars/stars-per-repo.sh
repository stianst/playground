#!/bin/bash -e

for i in $(cat repos); do
  echo $i $(gh api -X GET /repos/$i -q .stargazers_count) | awk '{ printf "%-35s %-40s\n", $1, $2}'
done