#!/bin/bash -e

gh api -X GET /repos/keycloak/keycloak/issues -F state=open --paginate > all-issues.json