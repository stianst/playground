#!/bin/bash -e

cat stars | cut -d ',' -f 2 | sort | awk -F '-' '{ print $1"-"$2 }' | uniq -c
