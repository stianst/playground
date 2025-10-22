#!/bin/bash -e

{ for i in $(cat repos); do
  echo $i $(gh api -X GET /repos/$i -q .stargazers_count) | awk '{ printf "%-10s %-40s\n", $2, $1}'
done } | sort -n -r
