#!/bin/bash -e

USERNAME=$(cat ~/.m2/settings.xml | grep username | cut -d '>' -f 2 | cut -d '<' -f 1)
PASSWORD=$(cat ~/.m2/settings.xml | grep password | cut -d '>' -f 2 | cut -d '<' -f 1)

printf "$USERNAME:$PASSWORD" | base64