#!/bin/bash -e

if [ "$1" == "" ] || [ "$2" == "" ]; then
  echo "create-env.sh <Keycloak Home> <ngrok domain>"
  exit 1
fi

function randomPassword() {
  tr -dc 'A-Za-z0-9' </dev/urandom | head -c 10
}

echo "KEYCLOAK_HOME=$1
KEYCLOAK_DOMAIN=$2
KEYCLOAK_URL=https://$2

KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=$(randomPassword)

REALM_NAME=openshift-demo

OPENSHIFT_CLIENT_ID=openshift

OC_CLIENT_ID=oc

USER_NAME=myuser
USER_PASSWORD=$(randomPassword)
USER_EMAIL=myuser@localhost.localdomain

OPENSHIFT_ADMIN=mykubeadmin
OPENSHIFT_ADMIN_PASSWORD=$(randomPassword)
OPENSHIFT_ADMIN_EMAIL=mykubeadmin@localhost.localdomain

GROUP_NAME=mygroup" > conf/env